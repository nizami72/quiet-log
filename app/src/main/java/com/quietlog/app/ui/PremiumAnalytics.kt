package com.quietlog.app.ui

import androidx.annotation.StringRes
import com.quietlog.app.R
import com.quietlog.app.data.local.entity.AttackEntity
import com.quietlog.app.data.local.entity.AttackMedicationCrossRef
import com.quietlog.app.data.local.entity.MedicationEntity
import java.time.Instant
import java.time.Month
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

private val ZONE = ZoneId.systemDefault()

/** Attacks at or above this intensity count as "severe" for correlation purposes. */
private const val SEVERE_INTENSITY_THRESHOLD = 7

data class MedicationStat(val name: String, val avgIntensity: Float, val attackCount: Int)

enum class DayPart(@StringRes val labelRes: Int, val hours: IntRange) {
    NIGHT(R.string.daypart_night, 0..5),
    MORNING(R.string.daypart_morning, 6..11),
    AFTERNOON(R.string.daypart_afternoon, 12..17),
    EVENING(R.string.daypart_evening, 18..23),
}

data class DayPartTrend(val part: DayPart, val count: Int, val avgIntensity: Float)

enum class Season(@StringRes val labelRes: Int) {
    WINTER(R.string.season_winter),
    SPRING(R.string.season_spring),
    SUMMER(R.string.season_summer),
    AUTUMN(R.string.season_autumn),
}

data class SeasonTrend(val season: Season, val count: Int, val avgIntensity: Float)

/** For each trigger tag, the % of attacks carrying it that were severe (>= [SEVERE_INTENSITY_THRESHOLD]). */
fun buildTriggerCorrelations(attacks: List<AttackEntity>): List<Pair<String, Int>> =
    attacks
        .flatMap { attack -> attack.triggers.map { it to attack.intensity } }
        .groupBy({ it.first }, { it.second })
        .mapValues { (_, intensities) -> (intensities.count { it >= SEVERE_INTENSITY_THRESHOLD } * 100) / intensities.size }
        .entries
        .sortedByDescending { it.value }
        .map { it.key to it.value }

/**
 * Average intensity of attacks logged alongside each medication. This is a correlation from
 * logged severity, not a before/after measurement — the data model has no pre-medication
 * intensity to compare against, so it can't show an actual reduction.
 */
fun buildMedicationStats(
    attacks: List<AttackEntity>,
    medications: List<MedicationEntity>,
    crossRefs: List<AttackMedicationCrossRef>,
): List<MedicationStat> {
    val intensityByAttackId = attacks.associate { it.id to it.intensity }
    val medicationNamesById = medications.associate { it.id to it.name }
    return crossRefs
        .mapNotNull { ref -> intensityByAttackId[ref.attackId]?.let { ref.medicationId to it } }
        .groupBy({ it.first }, { it.second })
        .mapNotNull { (medicationId, intensities) ->
            val name = medicationNamesById[medicationId] ?: return@mapNotNull null
            MedicationStat(name, intensities.average().toFloat(), intensities.size)
        }
        .sortedBy { it.avgIntensity }
}

fun buildWeekdayTrends(attacks: List<AttackEntity>): List<StatsBucket> =
    attacks
        .groupBy { Instant.ofEpochMilli(it.timestampStart).atZone(ZONE).dayOfWeek }
        .entries
        .sortedBy { it.key.value }
        .map { (day, bucketAttacks) ->
            StatsBucket(
                label = day.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                count = bucketAttacks.size,
                avgIntensity = bucketAttacks.map { it.intensity }.average().toFloat(),
            )
        }

fun buildDayPartTrends(attacks: List<AttackEntity>): List<DayPartTrend> =
    attacks
        .groupBy { attack ->
            val hour = Instant.ofEpochMilli(attack.timestampStart).atZone(ZONE).hour
            DayPart.entries.first { hour in it.hours }
        }
        .entries
        .sortedBy { it.key.ordinal }
        .map { (part, bucketAttacks) ->
            DayPartTrend(part, bucketAttacks.size, bucketAttacks.map { it.intensity }.average().toFloat())
        }

fun buildSeasonTrends(attacks: List<AttackEntity>): List<SeasonTrend> =
    attacks
        .groupBy { Instant.ofEpochMilli(it.timestampStart).atZone(ZONE).month.toSeason() }
        .entries
        .sortedBy { it.key.ordinal }
        .map { (season, bucketAttacks) ->
            SeasonTrend(season, bucketAttacks.size, bucketAttacks.map { it.intensity }.average().toFloat())
        }

private fun Month.toSeason(): Season = when (this) {
    Month.DECEMBER, Month.JANUARY, Month.FEBRUARY -> Season.WINTER
    Month.MARCH, Month.APRIL, Month.MAY -> Season.SPRING
    Month.JUNE, Month.JULY, Month.AUGUST -> Season.SUMMER
    Month.SEPTEMBER, Month.OCTOBER, Month.NOVEMBER -> Season.AUTUMN
}
