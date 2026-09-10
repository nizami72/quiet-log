package com.quietlog.app.data.pdf

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.quietlog.app.data.local.entity.AttackEntity
import com.quietlog.app.ui.StatsBucket
import com.quietlog.app.ui.StatsPeriod
import com.quietlog.app.ui.buildStatsBuckets
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val PAGE_WIDTH = 595
private const val PAGE_HEIGHT = 842
private const val MARGIN = 40f
private const val LINE_HEIGHT = 16f

private val TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

class PdfReportGenerator @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    suspend fun generate(
        attacks: List<AttackEntity>,
        medicationNamesByAttackId: Map<Long, List<String>>,
        period: StatsPeriod,
    ): File = withContext(Dispatchers.IO) {
        val document = PdfDocument()
        val writer = PageWriter(document)

        writer.title("QuietLog — отчёт для врача")
        writer.text("Период: ${period.label}")
        writer.spacer()

        val avgIntensity = if (attacks.isEmpty()) 0.0 else attacks.map { it.intensity }.average()
        writer.text("Всего приступов: ${attacks.size}")
        writer.text("Средняя интенсивность: ${"%.1f".format(avgIntensity)}/10")
        writer.spacer()

        val buckets = buildStatsBuckets(attacks, byMonth = period.bucketsByMonth)
        if (buckets.isNotEmpty()) {
            writer.text("Частота приступов по периодам:", bold = true)
            writer.chart(buckets)
            writer.spacer()
        }

        writer.text("Список приступов:", bold = true)
        attacks.sortedByDescending { it.timestampStart }.forEach { attack ->
            writer.attackBlock(attack, medicationNamesByAttackId[attack.id].orEmpty())
        }

        writer.finish()

        val reportsDir = File(context.cacheDir, "reports").apply { mkdirs() }
        val file = File(reportsDir, "quietlog-report-${System.currentTimeMillis()}.pdf")
        FileOutputStream(file).use { document.writeTo(it) }
        document.close()
        file
    }

    private class PageWriter(private val document: PdfDocument) {
        private var page: PdfDocument.Page = newPage()
        private var canvas: Canvas = page.canvas
        private var y = MARGIN + LINE_HEIGHT

        private val titlePaint = Paint().apply { textSize = 20f; isFakeBoldText = true; color = Color.BLACK }
        private val boldPaint = Paint().apply { textSize = 12f; isFakeBoldText = true; color = Color.BLACK }
        private val textPaint = Paint().apply { textSize = 12f; color = Color.BLACK }
        private val barPaint = Paint().apply { color = Color.rgb(0x7A, 0x69, 0xE0) }

        private fun newPage(): PdfDocument.Page {
            val pageNumber = document.pages.size + 1
            return document.startPage(PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create())
        }

        private fun ensureSpace(height: Float) {
            if (y + height > PAGE_HEIGHT - MARGIN) {
                document.finishPage(page)
                page = newPage()
                canvas = page.canvas
                y = MARGIN + LINE_HEIGHT
            }
        }

        fun title(text: String) {
            ensureSpace(24f)
            canvas.drawText(text, MARGIN, y, titlePaint)
            y += 28f
        }

        fun text(text: String, bold: Boolean = false) {
            ensureSpace(LINE_HEIGHT)
            canvas.drawText(text, MARGIN, y, if (bold) boldPaint else textPaint)
            y += LINE_HEIGHT
        }

        fun spacer() {
            y += LINE_HEIGHT / 2
        }

        fun chart(buckets: List<StatsBucket>) {
            val chartHeight = 100f
            ensureSpace(chartHeight + LINE_HEIGHT * 2)
            val maxCount = (buckets.maxOfOrNull { it.count } ?: 1).coerceAtLeast(1)
            val chartWidth = PAGE_WIDTH - 2 * MARGIN
            val barSlot = chartWidth / buckets.size
            val barWidth = barSlot * 0.6f
            val baseline = y + chartHeight
            buckets.forEachIndexed { index, bucket ->
                val barHeight = chartHeight * (bucket.count.toFloat() / maxCount)
                val left = MARGIN + index * barSlot + (barSlot - barWidth) / 2
                canvas.drawText(bucket.count.toString(), left, baseline - barHeight - 4f, textPaint)
                canvas.drawRect(left, baseline - barHeight, left + barWidth, baseline, barPaint)
                canvas.drawText(bucket.label, left, baseline + LINE_HEIGHT, textPaint)
            }
            y = baseline + LINE_HEIGHT * 2
        }

        fun attackBlock(attack: AttackEntity, medicationNames: List<String>) {
            ensureSpace(LINE_HEIGHT * 2)
            val timestamp = Instant.ofEpochMilli(attack.timestampStart)
                .atZone(ZoneId.systemDefault())
                .format(TIMESTAMP_FORMATTER)
            text("$timestamp — интенсивность ${attack.intensity}/10", bold = true)
            if (attack.locationZones.isNotEmpty()) text("Локализация: ${attack.locationZones.joinToString(", ")}")
            if (attack.symptoms.isNotEmpty()) text("Симптомы: ${attack.symptoms.joinToString(", ")}")
            if (attack.triggers.isNotEmpty()) text("Триггеры: ${attack.triggers.joinToString(", ")}")
            if (medicationNames.isNotEmpty()) text("Медикаменты: ${medicationNames.joinToString(", ")}")
            if (!attack.note.isNullOrBlank()) text("Заметка: ${attack.note}")
            spacer()
        }

        fun finish() {
            document.finishPage(page)
        }
    }
}
