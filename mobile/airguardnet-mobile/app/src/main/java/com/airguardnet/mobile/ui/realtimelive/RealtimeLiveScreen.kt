package com.airguardnet.mobile.ui.realtimelive

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
fun RealtimeLiveScreen(
    onBackToDemo: () -> Unit,
    viewModel: RealtimeLiveViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Tiempo real conectado (backend)", style = MaterialTheme.typography.headlineSmall)
        Text(
            text = "Lecturas reales usando tu dispositivo principal. Se actualiza cada pocos segundos.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        when {
            state.device == null -> {
                Text("No hay dispositivo principal asignado. Revisa tu Perfil o intenta actualizar en Home.")
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = onBackToDemo) { Text("Volver al modo DEMO") }
            }
            state.isLoading && state.current == null -> {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    CircularProgressIndicator()
                    Text(" Obteniendo lecturas en tiempo real…", modifier = Modifier.padding(start = 8.dp))
                }
            }
            state.errorMessage != null -> {
                Text(state.errorMessage ?: "Error", color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { viewModel.manualRefresh() }) { Text("Reintentar") }
                    Button(onClick = onBackToDemo) { Text("Ir a modo DEMO") }
                }
            }
            state.isEmpty -> {
                Text("Aún no hay lecturas en el backend para este dispositivo.")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { viewModel.manualRefresh() }) { Text("Actualizar ahora") }
            }
            else -> {
                state.current?.let { reading ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Dispositivo: ${state.device?.deviceUid}", style = MaterialTheme.typography.titleSmall)
                            Text(
                                text = "PM2.5: ${reading.pm25 ?: "--"} µg/m³",
                                style = MaterialTheme.typography.headlineMedium,
                                color = reading.statusColor,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("PM1: ${reading.pm1 ?: "--"}")
                                Text("PM10: ${reading.pm10 ?: "--"}")
                            }
                            Text(
                                text = "Batería: ${reading.batteryLevel?.toInt() ?: 0}%",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                            Text(
                                text = "Estado: ${reading.riskLabel} (${reading.qualityPercent}% aire limpio)",
                                color = reading.statusColor,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(top = 6.dp)
                            )
                            Text(
                                text = "Última actualización: ${reading.recordedAtFormatted}",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Lecturas recientes", style = MaterialTheme.typography.titleMedium)
                    Button(onClick = { viewModel.manualRefresh() }) { Text("Actualizar ahora") }
                }

                if (state.history.isEmpty()) {
                    Text("Sin historial aún.", modifier = Modifier.padding(top = 8.dp))
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.history) { reading ->
                            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            "PM2.5: ${reading.pm25 ?: "--"}",
                                            color = reading.statusColor,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(reading.recordedAtFormatted, style = MaterialTheme.typography.bodySmall)
                                    }
                                    Text("PM1: ${reading.pm1 ?: "--"} / PM10: ${reading.pm10 ?: "--"}")
                                    Text(
                                        "Batería: ${reading.batteryLevel?.toInt() ?: 0}% · ${reading.riskLabel}",
                                        color = reading.statusColor,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
