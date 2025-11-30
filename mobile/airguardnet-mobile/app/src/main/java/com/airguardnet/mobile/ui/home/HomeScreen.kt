package com.airguardnet.mobile.ui.home

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
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel(), onHistory: () -> Unit) {
    val state by viewModel.state.collectAsState()
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Semáforo personal", style = MaterialTheme.typography.titleLarge)
            Button(onClick = { viewModel.refresh() }) { Text("Actualizar") }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                val reading = state.lastReading
                Text(text = "PM2.5", style = MaterialTheme.typography.labelLarge)
                Text(text = reading?.pm25?.toString() ?: "--", style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold))
                Row(modifier = Modifier.padding(top = 8.dp)) {
                    Text(text = "PM1: ${reading?.pm1 ?: "--"}")
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "PM10: ${reading?.pm10 ?: "--"}")
                }
                Text(text = "Índice de riesgo: ${reading?.riskIndex ?: "--"}", modifier = Modifier.padding(top = 8.dp))
                Text(text = "Calidad: ${reading?.airQualityPercent ?: "--"}%", modifier = Modifier.padding(top = 4.dp))
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onHistory, modifier = Modifier.fillMaxWidth()) { Text("Ver detalle") }
        if (state.isLoading) {
            Spacer(modifier = Modifier.height(12.dp))
            CircularProgressIndicator()
        }
    }
}
