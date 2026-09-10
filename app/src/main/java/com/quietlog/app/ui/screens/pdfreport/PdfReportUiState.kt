package com.quietlog.app.ui.screens.pdfreport

import android.net.Uri
import com.quietlog.app.ui.StatsPeriod

data class PdfReportUiState(
    val isPremium: Boolean = false,
    val period: StatsPeriod = StatsPeriod.MONTH,
    val isGenerating: Boolean = false,
    val pendingShareUri: Uri? = null,
)
