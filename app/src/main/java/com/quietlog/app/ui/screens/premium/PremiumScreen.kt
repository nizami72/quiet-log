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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
        topBar = { TopAppBar(title = { Text("QuietLog Premium") }) },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                "Разовая покупка, без подписки. Входит:",
                style = MaterialTheme.typography.titleMedium,
            )

            PREMIUM_FEATURES.forEach { feature ->
                Column {
                    Text(feature.title, style = MaterialTheme.typography.titleMedium)
                    Text(feature.description, style = MaterialTheme.typography.bodyLarge)
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
                            Text("Уже куплено", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }

                uiState.connectionState == BillingConnectionState.UNAVAILABLE -> {
                    Text(
                        "Покупка временно недоступна. Проверьте, что Google Play доступен на устройстве.",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Button(onClick = {}, enabled = false, modifier = Modifier.fillMaxWidth()) {
                        Text("Купить")
                    }
                }

                else -> {
                    Button(
                        onClick = { activity?.let(viewModel::purchase) },
                        enabled = activity != null,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(uiState.priceLabel?.let { "Купить за $it" } ?: "Купить")
                    }
                }
            }
        }
    }
}
