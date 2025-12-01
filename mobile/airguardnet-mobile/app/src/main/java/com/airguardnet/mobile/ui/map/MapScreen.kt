package com.airguardnet.mobile.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.airguardnet.mobile.domain.model.Hotspot
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private enum class PermissionState { UNKNOWN, GRANTED, DENIED, PERMANENTLY_DENIED }

@SuppressLint("MissingPermission")
@Composable
fun MapScreen(viewModel: MapViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val fusedClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var permissionState by remember { mutableStateOf(PermissionState.UNKNOWN) }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
        val granted = result[Manifest.permission.ACCESS_FINE_LOCATION] == true || result[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        permissionState = if (granted) PermissionState.GRANTED else PermissionState.DENIED
        if (granted) {
            requestLocation(fusedClient) { latLng ->
                viewModel.setUserLocation(latLng.latitude, latLng.longitude)
            }
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
    }

    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { ctx ->
            MapView(ctx).apply {
                onCreate(null)
                onResume()
                getMapAsync { map ->
                    map.uiSettings.isZoomControlsEnabled = true
                    map.uiSettings.isMyLocationButtonEnabled = false
                    map.isMyLocationEnabled = permissionState == PermissionState.GRANTED
                    val userLat = state.userLatitude
                    val userLng = state.userLongitude

                    val focusLocation = when {
                        userLat != null && userLng != null -> LatLng(userLat, userLng)
                        state.hotspots.isNotEmpty() -> {
                            val first = state.hotspots.first()
                            LatLng(first.latitude, first.longitude)
                        }
                        else -> null
                    }

                    drawHotspots(state.hotspots, map)
                    state.userLatitude?.let { lat ->
                        state.userLongitude?.let { lng ->
                            map.addMarker(
                                MarkerOptions()
                                    .position(LatLng(lat, lng))
                                    .title("Mi ubicación")
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                            )
                        }
                    }
                    map.setOnMarkerClickListener { marker ->
                        val hotspot = state.hotspots.firstOrNull { it.latitude == marker.position.latitude && it.longitude == marker.position.longitude }
                        viewModel.selectHotspot(hotspot)
                        false
                    }
                    focusLocation?.let { map.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 13f)) }
                }
            }
        }, update = { mapView ->
            mapView.getMapAsync { map ->
                map.clear()
                map.isMyLocationEnabled = permissionState == PermissionState.GRANTED
                drawHotspots(state.hotspots, map)
                state.userLatitude?.let { lat ->
                    state.userLongitude?.let { lng ->
                        map.addMarker(
                            MarkerOptions()
                                .position(LatLng(lat, lng))
                                .title("Mi ubicación")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        )
                        if (permissionState == PermissionState.GRANTED) {
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 13f))
                        }
                    }
                }
                map.setOnMarkerClickListener { marker ->
                    val hotspot = state.hotspots.firstOrNull { it.latitude == marker.position.latitude && it.longitude == marker.position.longitude }
                    viewModel.selectHotspot(hotspot)
                    false
                }
            }
        })

        when {
            state.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            state.hotspots.isEmpty() -> {
                Card(
                    modifier = Modifier.align(Alignment.TopCenter).padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                ) {
                    Text("No hay hotspots configurados", modifier = Modifier.padding(12.dp))
                }
            }
        }

        if (permissionState != PermissionState.GRANTED) {
            Card(
                modifier = Modifier.align(Alignment.TopCenter).padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
            ) {
                val message = when (permissionState) {
                    PermissionState.UNKNOWN -> "Solicitando permisos de ubicación..."
                    PermissionState.DENIED -> "Habilita la ubicación para centrar el mapa en tu posición."
                    PermissionState.PERMANENTLY_DENIED -> "Habilita permisos en Ajustes para ver tu ubicación."
                    PermissionState.GRANTED -> ""
                }
                Text(
                    text = message,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
                if (permissionState == PermissionState.DENIED) {
                    Text(
                        text = "Si no deseas volver a ver este mensaje, concede el permiso.",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                if (permissionState == PermissionState.PERMANENTLY_DENIED) {
                    ButtonToSettings()
                }
            }
        }

        FloatingActionButton(
            onClick = {
                if (permissionState == PermissionState.GRANTED) {
                    requestLocation(fusedClient) { latLng ->
                        viewModel.setUserLocation(latLng.latitude, latLng.longitude)
                    }
                } else {
                    permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
                }
            },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
        ) {
            Icon(Icons.Rounded.MyLocation, contentDescription = "Mi ubicación")
        }

        state.selectedHotspot?.let { hotspot ->
            Card(
                modifier = Modifier.align(Alignment.BottomStart).padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = hotspot.severity, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.size(8.dp))
                        val badgeColor = when (hotspot.severity.uppercase()) {
                            "CRITICAL" -> MaterialTheme.colorScheme.error
                            "HIGH" -> MaterialTheme.colorScheme.tertiary
                            else -> MaterialTheme.colorScheme.primary
                        }
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(badgeColor, RoundedCornerShape(6.dp))
                        )
                    }
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(text = "PM2.5: ${hotspot.pm25}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Severidad: ${hotspot.severity}", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = "Última alerta: ${dateFormat.format(Date(hotspot.recordedAt))}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
private fun ButtonToSettings() {
    val context = LocalContext.current
    androidx.compose.material3.Button(onClick = {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }, modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
        Text("Abrir ajustes")
    }
}

@SuppressLint("MissingPermission")
private fun requestLocation(client: com.google.android.gms.location.FusedLocationProviderClient, onLocation: (LatLng) -> Unit) {
    client.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null)
        .addOnSuccessListener { loc ->
            loc?.let { onLocation(LatLng(it.latitude, it.longitude)) }
        }
}

private fun drawHotspots(hotspots: List<Hotspot>, map: com.google.android.gms.maps.GoogleMap) {
    hotspots.forEach { hotspot ->
        val color = if (hotspot.severity.uppercase() == "CRITICAL") BitmapDescriptorFactory.HUE_RED else BitmapDescriptorFactory.HUE_ORANGE
        map.addMarker(
            MarkerOptions()
                .position(LatLng(hotspot.latitude, hotspot.longitude))
                .title("PM2.5 ${hotspot.pm25}")
                .icon(BitmapDescriptorFactory.defaultMarker(color))
        )
    }
}
