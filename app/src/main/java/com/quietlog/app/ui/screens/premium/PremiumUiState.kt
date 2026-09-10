package com.quietlog.app.ui.screens.premium

import com.quietlog.app.data.billing.BillingConnectionState

data class PremiumUiState(
    val isPremium: Boolean = false,
    val connectionState: BillingConnectionState = BillingConnectionState.CONNECTING,
    val priceLabel: String? = null,
)
