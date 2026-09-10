package com.quietlog.app.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.quietlog.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogAttackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(stringResource(R.string.home_week_summary_title), style = MaterialTheme.typography.titleMedium)

            if (uiState.weekAttackCount == 0) {
                Text(
                    stringResource(R.string.home_week_summary_none),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 8.dp),
                )
            } else {
                Text(
                    uiState.weekAttackCount.toString(),
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(top = 16.dp),
                )
                Text(stringResource(R.string.home_week_attack_count_label), style = MaterialTheme.typography.bodyLarge)

                Text(
                    "%.1f/10".format(uiState.weekAvgIntensity),
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(top = 16.dp),
                )
                Text(stringResource(R.string.insights_avg_intensity), style = MaterialTheme.typography.bodyLarge)
            }

            Button(
                onClick = onLogAttackClick,
                modifier = Modifier.padding(top = 32.dp),
            ) {
                Text(stringResource(R.string.attack_now))
            }
        }
    }
}
