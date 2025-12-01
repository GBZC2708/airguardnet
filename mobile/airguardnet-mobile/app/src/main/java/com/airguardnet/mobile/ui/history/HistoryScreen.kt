package com.airguardnet.mobile.ui.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HistoryScreen(viewModel: HistoryViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Historial de lecturas", style = MaterialTheme.typography.titleLarge)
            Button(onClick = { viewModel.refresh() }) { Text("Actualizar") }
        }
        Spacer(modifier = Modifier.padding(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            HistoryFilter.values().forEach { filter ->
                FilterChip(
                    selected = state.selectedFilter == filter,
                    onClick = { viewModel.loadHistory(filter) },
                    label = { Text(filter.label) }
                )
            }
        }
        Spacer(modifier = Modifier.padding(4.dp))
        when {
            state.isLoading -> {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    CircularProgressIndicator()
                }
            }
            state.errorMessage != null -> {
                Column(modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
                    Text(state.errorMessage ?: "Error", color = MaterialTheme.colorScheme.error)
                    Button(onClick = { viewModel.refresh() }, modifier = Modifier.padding(top = 8.dp)) { Text("Reintentar") }
                }
            }
            state.isEmpty -> {
                Text("No hay lecturas para el rango seleccionado", modifier = Modifier.padding(top = 12.dp))
            }
            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 12.dp)) {
                    items(state.readings) { reading ->
                        Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(reading.riskLabel, color = reading.statusColor, fontWeight = FontWeight.SemiBold)
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(reading.timestampFormatted, style = MaterialTheme.typography.bodySmall)
                                }
                                Text("PM2.5: ${reading.pm25 ?: "--"}")
                                Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) {
                                    Text("PM1: ${reading.pm1 ?: "--"}")
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text("PM10: ${reading.pm10 ?: "--"}")
                                }
                                reading.batteryLevel?.let { Text("Batería: ${it.toInt()}%", modifier = Modifier.padding(top = 4.dp)) }
                                Text("Calidad: ${reading.qualityPercent}%", modifier = Modifier.padding(top = 4.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
