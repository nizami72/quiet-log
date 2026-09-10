package com.quietlog.app.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import org.json.JSONArray

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE attacks ADD COLUMN pressureHpa REAL")
    }
}

/**
 * `locationZones`/`symptoms`/`triggers` used to store the Russian display text itself as the
 * value (e.g. "Тошнота"). They now store a stable, locale-independent key (e.g. "NAUSEA") so the
 * UI can localize the label without corrupting historical data or stats grouped by tag. This
 * remaps any existing rows written before that change; unrecognized values are left as-is.
 */
val MIGRATION_2_3 = object : Migration(2, 3) {
    private val locationZoneKeysByLegacyLabel = mapOf(
        "Лоб" to "FOREHEAD",
        "Левый висок" to "LEFT_TEMPLE",
        "Правый висок" to "RIGHT_TEMPLE",
        "Затылок" to "OCCIPUT",
        "Вся голова" to "WHOLE_HEAD",
    )
    private val symptomKeysByLegacyLabel = mapOf(
        "Тошнота" to "NAUSEA",
        "Светобоязнь" to "PHOTOPHOBIA",
        "Звукобоязнь" to "PHONOPHOBIA",
        "Аура" to "AURA",
    )
    private val triggerKeysByLegacyLabel = mapOf(
        "Стресс" to "STRESS",
        "Сон" to "SLEEP",
        "Еда" to "FOOD",
        "Гормональный цикл" to "HORMONAL_CYCLE",
    )

    override fun migrate(db: SupportSQLiteDatabase) {
        val cursor = db.query("SELECT id, locationZones, symptoms, triggers FROM attacks")
        cursor.use {
            val idIndex = it.getColumnIndexOrThrow("id")
            val locationZonesIndex = it.getColumnIndexOrThrow("locationZones")
            val symptomsIndex = it.getColumnIndexOrThrow("symptoms")
            val triggersIndex = it.getColumnIndexOrThrow("triggers")
            while (it.moveToNext()) {
                val id = it.getLong(idIndex)
                val locationZones = remapJsonArray(it.getString(locationZonesIndex), locationZoneKeysByLegacyLabel)
                val symptoms = remapJsonArray(it.getString(symptomsIndex), symptomKeysByLegacyLabel)
                val triggers = remapJsonArray(it.getString(triggersIndex), triggerKeysByLegacyLabel)
                db.execSQL(
                    "UPDATE attacks SET locationZones = ?, symptoms = ?, triggers = ? WHERE id = ?",
                    arrayOf(locationZones, symptoms, triggers, id),
                )
            }
        }
    }

    private fun remapJsonArray(json: String?, keysByLegacyLabel: Map<String, String>): String {
        if (json.isNullOrBlank()) return "[]"
        val source = JSONArray(json)
        val remapped = JSONArray()
        for (i in 0 until source.length()) {
            val value = source.getString(i)
            remapped.put(keysByLegacyLabel[value] ?: value)
        }
        return remapped.toString()
    }
}
