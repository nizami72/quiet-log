package com.quietlog.app.ui

import androidx.annotation.StringRes
import com.quietlog.app.R

data class PremiumFeature(@StringRes val titleRes: Int, @StringRes val descriptionRes: Int)

val PREMIUM_FEATURES = listOf(
    PremiumFeature(
        R.string.premium_feature_trigger_analysis_title,
        R.string.premium_feature_trigger_analysis_description,
    ),
    PremiumFeature(
        R.string.premium_feature_medication_effectiveness_title,
        R.string.premium_feature_medication_effectiveness_description,
    ),
    PremiumFeature(
        R.string.premium_feature_cycle_correlation_title,
        R.string.premium_feature_cycle_correlation_description,
    ),
    PremiumFeature(
        R.string.premium_feature_pdf_report_title,
        R.string.premium_feature_pdf_report_description,
    ),
    PremiumFeature(
        R.string.premium_feature_extended_trends_title,
        R.string.premium_feature_extended_trends_description,
    ),
)
