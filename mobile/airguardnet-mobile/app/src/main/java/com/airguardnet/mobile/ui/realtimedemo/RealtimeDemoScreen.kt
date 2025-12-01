package com.airguardnet.mobile.ui.realtimedemo

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlin.math.max

@Composable
fun RealtimeDemoScreen(viewModel: RealtimeDemoViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val current = state.current
    val (statusText, statusColor) = severityFor(current?.pm25)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Simulador en tiempo real (demo)", style = MaterialTheme.typography.headlineSmall)
        Text(
            text = "MODO DEMO – Estos datos son simulados. NO provienen del backend.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Lectura actual", style = MaterialTheme.typography.titleMedium)
                if (current != null) {
                    Text(
                        text = "PM2.5: ${"%.1f".format(current.pm25)} µg/m³",
                        style = MaterialTheme.typography.headlineSmall,
                        color = statusColor,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "PM1: ${"%.1f".format(current.pm1)}")
                        Text(text = "PM10: ${"%.1f".format(current.pm10)}")
                    }
                    Text(
                        text = "Estado: $statusText",
                        color = statusColor,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                } else {
                    Text(text = "Esperando primeras lecturas...")
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Evolución reciente (PM2.5)", style = MaterialTheme.typography.titleSmall)
        DemoChart(readings = state.readings)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(onClick = { viewModel.toggleSimulation() }, modifier = Modifier.weight(1f)) {
                Text(if (state.isRunning) "Pausar" else "Reanudar")
            }
            Button(onClick = { viewModel.clearHistory() }, modifier = Modifier.weight(1f)) {
                Text("Limpiar")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Actualiza cada ~2.5 segundos. En el futuro se puede conectar a lecturas reales.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun DemoChart(readings: List<DemoReading>) {
    val points = readings.takeLast(20)
    val maxValue = max(points.maxOfOrNull { it.pm25 } ?: 1f, 1f)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        if (points.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Aún no hay datos simulados")
            }
        } else {
            Canvas(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                val barWidth = size.width / points.size
                points.forEachIndexed { index, reading ->
                    val barHeight = (reading.pm25 / maxValue) * size.height
                    drawRoundRect(
                        color = MaterialTheme.colorScheme.primary,
                        topLeft = Offset(
                            x = barWidth * index + barWidth * 0.1f,
                            y = size.height - barHeight
                        ),
                        size = Size(barWidth * 0.8f, barHeight),
                        cornerRadius = CornerRadius(8f, 8f)
                    )
                }
                drawRect(
                    color = MaterialTheme.colorScheme.outline,
                    style = Stroke(width = 2f),
                    size = size
                )
            }
        }
    }
}

@Composable
private fun severityFor(pm25: Float?): Pair<String, Color> {
    if (pm25 == null) return "Sin datos" to MaterialTheme.colorScheme.onSurface
    return when {
        pm25 < 12f -> "Bueno" to Color(0xFF4CAF50)
        pm25 < 35f -> "Moderado" to Color(0xFFFFC107)
        pm25 < 55f -> "Malo" to Color(0xFFFF9800)
        else -> "Crítico" to Color(0xFFF44336)
    }
}
