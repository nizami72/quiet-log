package com.quietlog.app.ui.screens.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.quietlog.app.R
import com.quietlog.app.ui.components.PlaceholderScreen

@Composable
fun HomeScreen(
    onLogAttackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PlaceholderScreen(title = stringResource(R.string.app_name), modifier = modifier) {
        Text(stringResource(R.string.home_weekly_summary_placeholder))
        Button(
            onClick = onLogAttackClick,
            modifier = Modifier.padding(top = 24.dp),
        ) {
            Text(stringResource(R.string.attack_now))
        }
    }
}
