package com.quietlog.app.ui.screens.premium

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.quietlog.app.R
import com.quietlog.app.data.billing.BillingConnectionState
import com.quietlog.app.ui.PREMIUM_FEATURES

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumScreen(
    modifier: Modifier = Modifier,
    viewModel: PremiumViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val activity = LocalContext.current.findActivity()

    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text(stringResource(R.string.premium_brand_title)) }) },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                stringResource(R.string.premium_one_time_purchase_intro),
                style = MaterialTheme.typography.titleMedium,
            )

            PREMIUM_FEATURES.forEach { feature ->
                Column {
                    Text(stringResource(feature.titleRes), style = MaterialTheme.typography.titleMedium)
                    Text(stringResource(feature.descriptionRes), style = MaterialTheme.typography.bodyLarge)
                }
            }

            when {
                uiState.isPremium -> {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Icon(Icons.Filled.CheckCircle, contentDescription = null)
                            Text(stringResource(R.string.premium_already_purchased), style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }

                uiState.connectionState == BillingConnectionState.UNAVAILABLE -> {
                    Text(
                        stringResource(R.string.premium_unavailable),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Button(onClick = {}, enabled = false, modifier = Modifier.fillMaxWidth()) {
                        Text(stringResource(R.string.action_buy))
                    }
                }

                else -> {
                    Button(
                        onClick = { activity?.let(viewModel::purchase) },
                        enabled = activity != null,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            uiState.priceLabel?.let { stringResource(R.string.format_buy_with_price, it) }
                                ?: stringResource(R.string.action_buy),
                        )
                    }
                }
            }
        }
    }
}
