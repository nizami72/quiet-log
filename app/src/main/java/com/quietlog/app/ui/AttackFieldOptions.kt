package com.quietlog.app.ui

import androidx.annotation.StringRes
import com.quietlog.app.R

/**
 * Stored in Room as `name` (e.g. "FOREHEAD") so the value stays a stable, locale-independent
 * key — only the displayed [labelRes] changes with the active language.
 */
enum class LocationZone(@StringRes val labelRes: Int) {
    FOREHEAD(R.string.location_zone_forehead),
    LEFT_TEMPLE(R.string.location_zone_left_temple),
    RIGHT_TEMPLE(R.string.location_zone_right_temple),
    OCCIPUT(R.string.location_zone_occiput),
    WHOLE_HEAD(R.string.location_zone_whole_head),
    ;

    companion object {
        fun fromKey(key: String): LocationZone? = entries.find { it.name == key }
    }
}

enum class Symptom(@StringRes val labelRes: Int) {
    NAUSEA(R.string.symptom_nausea),
    PHOTOPHOBIA(R.string.symptom_photophobia),
    PHONOPHOBIA(R.string.symptom_phonophobia),
    AURA(R.string.symptom_aura),
    ;

    companion object {
        fun fromKey(key: String): Symptom? = entries.find { it.name == key }
    }
}

enum class Trigger(@StringRes val labelRes: Int) {
    STRESS(R.string.trigger_stress),
    SLEEP(R.string.trigger_sleep),
    FOOD(R.string.trigger_food),
    HORMONAL_CYCLE(R.string.trigger_hormonal_cycle),
    ;

    companion object {
        fun fromKey(key: String): Trigger? = entries.find { it.name == key }
    }
}
