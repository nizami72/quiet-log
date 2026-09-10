package com.quietlog.app.ui.screens.premium

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quietlog.app.data.billing.BillingRepository
import com.quietlog.app.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class PremiumViewModel @Inject constructor(
    settingsRepository: SettingsRepository,
    private val billingRepository: BillingRepository,
) : ViewModel() {

    val uiState: StateFlow<PremiumUiState> = combine(
        settingsRepository.settings,
        billingRepository.connectionState,
        billingRepository.productDetails,
    ) { settings, connectionState, productDetails ->
        PremiumUiState(
            isPremium = settings.premiumStatus,
            connectionState = connectionState,
            priceLabel = productDetails?.oneTimePurchaseOfferDetails?.formattedPrice,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PremiumUiState(),
    )

    fun purchase(activity: Activity) {
        billingRepository.launchPurchase(activity)
    }
}
