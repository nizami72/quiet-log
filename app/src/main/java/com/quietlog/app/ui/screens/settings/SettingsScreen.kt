package com.quietlog.app.ui.screens.settings

import android.content.Intent
import androidx.core.net.toUri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.quietlog.app.R
import com.quietlog.app.debug.DebugToolsSection

private const val PRIVACY_POLICY_URL = "https://sites.google.com/view/privacypolicyforquietlog/privacy-policy"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateToMedications: () -> Unit,
    onNavigateToPremium: () -> Unit,
    onNavigateToPdfReport: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text(stringResource(R.string.settings_title)) }) },
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            ListItem(
                headlineContent = { Text(stringResource(R.string.label_medications)) },
                leadingContent = { Icon(Icons.Filled.Medication, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onNavigateToMedications),
            )
            ListItem(
                headlineContent = { Text(stringResource(R.string.settings_item_pdf_report)) },
                leadingContent = { Icon(Icons.Filled.Description, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onNavigateToPdfReport),
            )
            ListItem(
                headlineContent = { Text(stringResource(R.string.premium_brand_title)) },
                leadingContent = { Icon(Icons.Filled.WorkspacePremium, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onNavigateToPremium),
            )
            ListItem(
                headlineContent = { Text(stringResource(R.string.settings_item_privacy_policy)) },
                leadingContent = { Icon(Icons.Filled.PrivacyTip, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = {
                        context.startActivity(Intent(Intent.ACTION_VIEW, PRIVACY_POLICY_URL.toUri()))
                    }),
            )

            DebugToolsSection()
        }
    }
}
