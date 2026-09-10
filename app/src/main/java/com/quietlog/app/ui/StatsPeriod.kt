package com.quietlog.app.ui

import java.time.ZoneId
import java.time.ZonedDateTime

enum class StatsPeriod(val label: String) {
    WEEK("Неделя"),
    MONTH("Месяц"),
    THREE_MONTHS("3 месяца"),
    ALL_TIME("Всё время");

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
