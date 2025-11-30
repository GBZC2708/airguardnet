package com.airguardnet.mobile.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Perfil", style = MaterialTheme.typography.titleLarge)
                Text(state.session?.name ?: "")
                Text(state.session?.email ?: "")
                Text("Rol: ${state.session?.role ?: ""}")
                Text("Plan: ${state.session?.planId ?: ""}")
                Text("Dispositivo: ${state.device?.name ?: "Sin asignar"}")
            }
        }
        Button(onClick = { viewModel.logout() }, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) { Text("Cerrar sesión") }
    }
}
