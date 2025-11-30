package com.airguardnet.mobile.ui.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(viewModel: HistoryViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = { viewModel.refresh() }, modifier = Modifier.fillMaxWidth()) { Text("Actualizar") }
        LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 12.dp)) {
            items(state.readings) { reading ->
                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Hora: ${formatter.format(Date(reading.recordedAt))}", style = MaterialTheme.typography.labelMedium)
                        Text("PM2.5: ${reading.pm25 ?: "--"}")
                        Text("Riesgo: ${reading.riskIndex}")
                    }
                }
            }
        }
    }
}
