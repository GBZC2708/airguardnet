package com.airguardnet.mobile.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ProfileScreen(onLoggedOut: () -> Unit = {}, viewModel: ProfileViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Perfil", style = MaterialTheme.typography.titleLarge)
                if (state.isLoading) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                        CircularProgressIndicator(modifier = Modifier.padding(end = 8.dp))
                        Text("Cargando perfil...")
                    }
                }
                state.errorMessage?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
                    Button(onClick = { viewModel.refresh() }, modifier = Modifier.padding(top = 8.dp)) { Text("Reintentar") }
                }
                state.session?.let { session ->
                    Text("Nombre: ${session.name}", modifier = Modifier.padding(top = 8.dp))
                    Text("Email: ${session.email}")
                    Text("Rol: ${session.role}")
                    Text("Plan: ${state.planName}")
                }
                Text("Dispositivo: ${state.device?.let { "${it.deviceUid} – ${it.name}" } ?: "Sin asignar"}", modifier = Modifier.padding(top = 8.dp))
                Text("Alertas críticas últimas 24h: ${state.criticalAlertsLast24h}")
                Text("Última lectura: ${state.lastReadingSummary ?: "Sin lecturas"}")
                RowWithSwitch(
                    label = "Notificaciones críticas",
                    checked = state.criticalNotifications,
                    onCheckedChange = viewModel::toggleNotifications
                )
            }
        }
        Button(onClick = { viewModel.logout(); onLoggedOut() }, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            Text("Cerrar sesión")
        }
    }
}

@Composable
private fun RowWithSwitch(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
