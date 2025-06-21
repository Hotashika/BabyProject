package com.example.bebegim.ui.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb

@Composable
fun LineChart(
    data: List<Float>,
    labels: List<String>,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary,
    pointColor: Color = MaterialTheme.colorScheme.primary,
    showGrid: Boolean = false,
    showValuesOnPoints: Boolean = false,
    curvedLine: Boolean = false,
    fixedYAxisRange: Pair<Float, Float>? = null // Yeni parametre
) {
    val textColor = MaterialTheme.colorScheme.onSurface

    val (minValue, maxValue) = fixedYAxisRange ?: run {
        val min = data.minOrNull() ?: 0f
        val max = data.maxOrNull() ?: 0f
        val padding = (max - min) * 0.2f
        min - padding to max + padding
    }

    val range = maxValue - minValue

    Canvas(modifier = modifier) {
        val height = size.height
        val width = size.width
        val padding = 40f
        val xStep = (width - 2 * padding) / (data.size - 1)

        val points = data.mapIndexed { index, value ->
            val x = padding + 30f + index * xStep
            val y = height - padding - ((value - minValue) / range) * (height - 2 * padding) - 15f
            Offset(x, y)
        }

        // Grid çizgileri
        if (showGrid) {
            val yStep = (height - 2 * padding) / 4
            for (i in 0..4) {
                val y = padding + i * yStep
                drawLine(
                    color = Color.LightGray.copy(alpha = 0.2f),
                    start = Offset(padding, y),
                    end = Offset(width - padding, y),
                    strokeWidth = 1f
                )
            }
        }

        // X ve Y eksen çizgileri
        drawLine(
            color = textColor.copy(alpha = 0.5f),
            start = Offset(padding, padding),
            end = Offset(padding, height - padding),
            strokeWidth = 2f
        )
        drawLine(
            color = textColor.copy(alpha = 0.5f),
            start = Offset(padding, height - padding),
            end = Offset(width - padding, height - padding),
            strokeWidth = 2f
        )

        // Çizgi (henüz gerçek curve değil)
        for (i in 0 until points.size - 1) {
            drawLine(
                color = lineColor,
                start = points[i],
                end = points[i + 1],
                strokeWidth = 3f
            )
        }

        // Noktalar
        points.forEachIndexed { index, point ->
            drawCircle(
                color = pointColor,
                radius = 6f,
                center = point
            )

            // Nokta değeri gösterimi
            if (showValuesOnPoints) {
                drawContext.canvas.nativeCanvas.drawText(
                    String.format("%.1f", data[index]),
                    point.x +30f,
                    point.y - 15,
                    Paint().apply {
                        color = textColor.toArgb()
                        textAlign = Paint.Align.CENTER
                        textSize = 28f
                    }
                )
            }
        }

        // Etiketler (X)
        val labelPaint = Paint().apply {
            color = textColor.toArgb()
            textAlign = Paint.Align.CENTER
            textSize = 30f
        }

        labels.forEachIndexed { index, label ->
            val x = padding + 30f + index * xStep
            drawContext.canvas.nativeCanvas.drawText(
                label,
                x,
                height - padding / 6,
                labelPaint
            )
        }

        // Etiketler (Y)
        val yLabelPaint = Paint().apply {
            color = textColor.toArgb()
            textAlign = Paint.Align.RIGHT
            textSize = 28f
        }

        val yStep = (height - 2 * padding) / 4
        for (i in 0..4) {
            val y = height - padding - i * yStep
            val value = minValue + (i / 4f) * range
            drawContext.canvas.nativeCanvas.drawText(
                String.format("%.1f", value),
                padding - 10,
                y ,
                yLabelPaint
            )
        }
    }
}


@Composable
fun BarChart(
    data: List<Pair<String, Float>>,
    maxValue: Float,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    val textColor = MaterialTheme.colorScheme.onSurface

    Canvas(modifier = modifier) {
        val height = size.height
        val width = size.width
        val padding = 40f
        val barWidth = (width - 2 * padding) / data.size - 20f

        // Draw x and y axis
        drawLine(
            color = textColor.copy(alpha = 0.5f),
            start = Offset(padding, padding),
            end = Offset(padding, height - padding),
            strokeWidth = 2f
        )

        drawLine(
            color = textColor.copy(alpha = 0.5f),
            start = Offset(padding, height - padding),
            end = Offset(width - padding, height - padding),
            strokeWidth = 2f
        )

        // Draw bars
        data.forEachIndexed { index, (label, value) ->
            val x = padding + index * ((width - 2 * padding) / data.size) + 10f
            val barHeight = (value / maxValue) * (height - 2 * padding)

            val color = when (index % 3) {
                0 -> primaryColor
                1 -> secondaryColor
                else -> tertiaryColor
            }

            drawRect(
                color = color,
                topLeft = Offset(x, height - padding - barHeight),
                size = Size(barWidth, barHeight)
            )

            // Draw label
            val textPaint = Paint().apply {
                this.color = textColor.toArgb()
                textAlign = Paint.Align.CENTER
                textSize = 30f
            }

            drawContext.canvas.nativeCanvas.drawText(
                label,
                x + barWidth / 2,
                height - padding / 2,
                textPaint
            )

            // Draw value on top of bar
            drawContext.canvas.nativeCanvas.drawText(
                String.format("%.0f", value),
                x + barWidth / 2,
                height - padding - barHeight - 10,
                textPaint
            )
        }

        // Y-axis labels
        val textPaint = Paint().apply {
            color = textColor.toArgb()
            textAlign = Paint.Align.RIGHT
            textSize = 30f
        }

        val yStep = (height - 2 * padding) / 4
        for (i in 0..4) {
            val y = height - padding - i * yStep
            val value = (i / 4f) * maxValue
            drawContext.canvas.nativeCanvas.drawText(
                String.format("%.0f", value),
                padding - 10,
                y + 10,
                textPaint
            )
        }
    }
}