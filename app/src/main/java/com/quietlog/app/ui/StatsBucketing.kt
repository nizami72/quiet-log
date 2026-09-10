package com.quietlog.app.ui

import com.quietlog.app.data.local.entity.AttackEntity
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

private val ZONE = ZoneId.systemDefault()
private val RU = Locale("ru")
private val WEEK_LABEL_FORMATTER = DateTimeFormatter.ofPattern("dd.MM", RU)
private val MONTH_LABEL_FORMATTER = DateTimeFormatter.ofPattern("LLL yyyy", RU)

/** Groups attacks by week (Monday-start) or by calendar month, sorted chronologically. */
fun buildStatsBuckets(attacks: List<AttackEntity>, byMonth: Boolean): List<StatsBucket> {
    return attacks
        .groupBy { bucketStart(it.timestampStart, byMonth) }
        .entries
        .sortedBy { it.key.toEpochDay() }
        .map { (bucketStart, bucketAttacks) ->
            StatsBucket(
                label = bucketStart.format(if (byMonth) MONTH_LABEL_FORMATTER else WEEK_LABEL_FORMATTER),
                count = bucketAttacks.size,
                avgIntensity = bucketAttacks.map { it.intensity }.average().toFloat(),
            )
        }
}

private fun bucketStart(timestampMillis: Long, byMonth: Boolean): LocalDate {
    val date = Instant.ofEpochMilli(timestampMillis).atZone(ZONE).toLocalDate()
    return if (byMonth) {
        date.withDayOfMonth(1)
    } else {
        date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    }
}
