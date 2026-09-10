package com.quietlog.app.debug

import com.quietlog.app.data.local.entity.AttackEntity
import com.quietlog.app.data.local.entity.MedicationEntity
import com.quietlog.app.data.repository.AttackRepository
import com.quietlog.app.data.repository.MedicationRepository
import com.quietlog.app.ui.LocationZone
import com.quietlog.app.ui.Symptom
import com.quietlog.app.ui.Trigger
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject
import kotlin.random.Random
import kotlinx.coroutines.flow.first

/**
 * Debug-only. This whole file lives under `src/debug`, so it — and the Hilt bindings it
 * pulls in — are absent from release builds at compile time, not just unreachable at runtime.
 *
 * Fills the local database with a couple of months of plausible-looking attacks (and a few
 * sample medications) so PDF export and Insights have enough data to preview meaningfully.
 * [clearSeedData] removes exactly what this class added, identified by [SEED_NOTE_MARKER] on
 * attacks and by name for medications — real user data is never touched.
 */
class DebugDataSeeder @Inject constructor(
    private val attackRepository: AttackRepository,
    private val medicationRepository: MedicationRepository,
) {
    private val seedMedications = listOf(
        "Ibuprofen 400mg" to "1 tablet",
        "Sumatriptan 50mg" to "1 tablet",
        "Paracetamol 500mg" to "2 tablets",
    )

    suspend fun seedSampleData(monthsBack: Int = 2, attackCount: Int = 40) {
        val medicationIds = seedMedications.map { (name, dosage) ->
            medicationRepository.saveMedication(
                MedicationEntity(name = name, dosage = dosage, createdAt = System.currentTimeMillis()),
            )
        }

        val now = ZonedDateTime.now(ZoneId.systemDefault())
        val startMillis = now.minusMonths(monthsBack.toLong()).toInstant().toEpochMilli()
        val endMillis = now.toInstant().toEpochMilli()

        repeat(attackCount) {
            val attackId = attackRepository.saveAttack(
                AttackEntity(
                    timestampStart = Random.nextLong(startMillis, endMillis),
                    intensity = Random.nextInt(1, 11),
                    locationZones = LocationZone.entries.shuffled().take(Random.nextInt(0, 3)).map { it.name },
                    symptoms = Symptom.entries.shuffled().take(Random.nextInt(0, 3)).map { it.name },
                    triggers = Trigger.entries.shuffled().take(Random.nextInt(0, 3)).map { it.name },
                    pressureHpa = 990f + Random.nextFloat() * 40f,
                    note = SEED_NOTE_MARKER,
                ),
            )
            if (Random.nextBoolean()) {
                attackRepository.setMedicationsForAttack(attackId, listOf(medicationIds.random()))
            }
        }
    }

    suspend fun clearSeedData() {
        attackRepository.observeAttacks().first()
            .filter { it.note == SEED_NOTE_MARKER }
            .forEach { attackRepository.deleteAttack(it) }
        medicationRepository.observeMedications().first()
            .filter { medication -> seedMedications.any { it.first == medication.name } }
            .forEach { medicationRepository.deleteMedication(it) }
    }

    private companion object {
        const val SEED_NOTE_MARKER = "[quietlog-seed-data]"
    }
}
