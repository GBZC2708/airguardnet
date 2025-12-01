package com.airguardnet.mobile.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onHistory: () -> Unit,
    onRealtimeDemo: () -> Unit,
    onRealtimeLive: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Semáforo personal", style = MaterialTheme.typography.titleLarge)
            Button(onClick = { viewModel.refresh() }) { Text("Actualizar") }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                when {
                    state.isLoading -> {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(modifier = Modifier.padding(end = 12.dp))
                            Text("Cargando última lectura...")
                        }
                    }
                    state.errorMessage != null -> {
                        Text(text = state.errorMessage ?: "Error", color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.refresh() }) { Text("Reintentar") }
                    }
                    state.isEmpty -> {
                        Text("Aún no hay lecturas para tu dispositivo")
                    }
                    else -> {
                        val reading = state.lastReading
                        Text(text = "PM2.5", style = MaterialTheme.typography.labelLarge)
                        Text(text = reading?.pm25?.toString() ?: "--", style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold))
                        reading?.let {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                BadgeLabel(label = it.riskLabel, color = it.statusColor)
                                Spacer(modifier = Modifier.weight(1f))
                                Text("Calidad: ${it.qualityPercent}%")
                            }
                            Row(modifier = Modifier.padding(top = 4.dp)) {
                                Text(text = "PM1: ${it.pm1 ?: "--"}")
                                Spacer(modifier = Modifier.weight(1f))
                                Text(text = "PM10: ${it.pm10 ?: "--"}")
                            }
                            Text(text = "Batería: ${it.batteryLevel?.toInt() ?: 0}%", modifier = Modifier.padding(top = 6.dp))
                            it.timestampFormatted?.let { ts ->
                                Text(text = "Última lectura: $ts", modifier = Modifier.padding(top = 6.dp), style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onRealtimeDemo, modifier = Modifier.fillMaxWidth()) { Text("Simulador en tiempo real (demo)") }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRealtimeLive, modifier = Modifier.fillMaxWidth()) { Text("Tiempo real conectado (backend)") }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onHistory, modifier = Modifier.fillMaxWidth()) { Text("Ver detalle") }
    }
}

@Composable
private fun BadgeLabel(label: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(label, color = color, fontWeight = FontWeight.SemiBold)
    }
}
