package com.airguardnet.mobile.ui.alerts

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AlertsScreen(defaultFilter: String? = null, viewModel: AlertsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(defaultFilter) {
        defaultFilter?.let { viewModel.applyInitialFilter(AlertFilter.fromString(it)) }
    }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Alertas", style = MaterialTheme.typography.titleLarge)
            Button(onClick = { viewModel.refresh() }) { Text("Actualizar") }
        }
        Spacer(modifier = Modifier.padding(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AlertFilter.values().forEach { filter ->
                FilterChip(
                    selected = state.selectedFilter == filter,
                    onClick = { viewModel.loadAlerts(filter) },
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
                Text("No hay alertas para este filtro", modifier = Modifier.padding(top = 12.dp))
            }
            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 12.dp)) {
                    items(state.alerts) { alert ->
                        val color = when {
                            alert.isCritical -> Color.Red
                            alert.severity.equals("HIGH", true) -> Color(0xFFFF9800)
                            alert.severity.equals("WARNING", true) -> Color(0xFFFFC107)
                            else -> MaterialTheme.colorScheme.primary
                        }
                        Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(alert.severity, color = color, style = MaterialTheme.typography.titleMedium)
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(alert.timestampFormatted, style = MaterialTheme.typography.bodySmall)
                                }
                                Text(alert.message)
                                alert.deviceUid?.let { Text("Dispositivo: $it", modifier = Modifier.padding(top = 4.dp)) }
                            }
                        }
                    }
                }
            }
        }
    }
}
