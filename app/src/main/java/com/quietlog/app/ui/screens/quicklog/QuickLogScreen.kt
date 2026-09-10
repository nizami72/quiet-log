package com.quietlog.app.ui.screens.quicklog

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.quietlog.app.ui.components.IntensityPicker
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickLogScreen(
    onSaved: (attackId: Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QuickLogViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()
    var isSaving by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text("Приступ сейчас") }) },
    ) { padding ->
        IntensityPicker(
            selected = null,
            enabled = !isSaving,
            onSelect = { intensity ->
                isSaving = true
                scope.launch {
                    val attackId = viewModel.logAttack(intensity)
                    onSaved(attackId)
                }
            },
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
        )
    }
}
