package com.quietlog.app.ui.screens.logdetails

data class LogDetailsUiState(
    val intensity: Int? = null,
    val locationZones: Set<String> = emptySet(),
    val symptoms: Set<String> = emptySet(),
    val triggers: Set<String> = emptySet(),
    val note: String = "",
) {
    companion object {
        val LOCATION_ZONE_OPTIONS = listOf("Лоб", "Левый висок", "Правый висок", "Затылок", "Вся голова")
        val SYMPTOM_OPTIONS = listOf("Тошнота", "Светобоязнь", "Звукобоязнь", "Аура")
        val TRIGGER_OPTIONS = listOf("Стресс", "Сон", "Еда", "Гормональный цикл")
    }
}
