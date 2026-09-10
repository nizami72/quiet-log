package com.quietlog.app.ui

import androidx.annotation.StringRes
import com.quietlog.app.R
import java.time.ZoneId
import java.time.ZonedDateTime

enum class StatsPeriod(@StringRes val labelRes: Int) {
    WEEK(R.string.period_week),
    MONTH(R.string.period_month),
    THREE_MONTHS(R.string.period_three_months),
    ALL_TIME(R.string.period_all_time);

    /** Inclusive lower bound in epoch millis, or null for no lower bound. */
    fun startMillis(now: ZonedDateTime = ZonedDateTime.now(ZoneId.systemDefault())): Long? = when (this) {
        WEEK -> now.minusWeeks(1).toInstant().toEpochMilli()
        MONTH -> now.minusMonths(1).toInstant().toEpochMilli()
        THREE_MONTHS -> now.minusMonths(3).toInstant().toEpochMilli()
        ALL_TIME -> null
    }

    /** Whether chart buckets should be grouped by month instead of by week. */
    val bucketsByMonth: Boolean get() = this == ALL_TIME
}
