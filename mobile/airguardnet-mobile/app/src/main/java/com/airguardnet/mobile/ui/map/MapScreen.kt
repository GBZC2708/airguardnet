package com.airguardnet.mobile.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.airguardnet.mobile.domain.model.Hotspot
import com.google.android.gms.location.FusedLocationProviderClient
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

/**
 * Muestra el mapa con hotspots y controla el permiso de ubicación. No bloquea el mapa cuando
 * el permiso es denegado y recentra en la ubicación del usuario al obtenerla.
 */
@SuppressLint("MissingPermission")
@Composable
fun MapScreen(viewModel: MapViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val activity = remember { context.findActivity() }
    val fusedClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val mapView = remember {
        MapView(context).apply {
            onCreate(null)
            onResume()
        }
    }
    var permissionState by remember { mutableStateOf(PermissionState.UNKNOWN) }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
        val granted = result[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            result[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        permissionState = when {
            granted -> PermissionState.GRANTED
            activity?.let {
                !ActivityCompat.shouldShowRequestPermissionRationale(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) && !ActivityCompat.shouldShowRequestPermissionRationale(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } == true -> PermissionState.PERMANENTLY_DENIED
            else -> PermissionState.DENIED
        }
        if (granted) {
            requestLocation(fusedClient) { latLng ->
                viewModel.setUserLocation(latLng.latitude, latLng.longitude)
            }
        }
    }

    LaunchedEffect(Unit) {
        val hasFine = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        val hasCoarse = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        permissionState = if (hasFine || hasCoarse) PermissionState.GRANTED else PermissionState.UNKNOWN
        if (permissionState == PermissionState.GRANTED) {
            requestLocation(fusedClient) { latLng ->
                viewModel.setUserLocation(latLng.latitude, latLng.longitude)
            }
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { mapView }, update = { view ->
            view.getMapAsync { map ->
                map.uiSettings.isZoomControlsEnabled = true
                map.uiSettings.isMyLocationButtonEnabled = false
                map.isMyLocationEnabled = permissionState == PermissionState.GRANTED
                map.clear()

                drawHotspots(state.hotspots, map)
                val userLatLng = state.userLatitude?.let { lat ->
                    state.userLongitude?.let { lng -> LatLng(lat, lng) }
                }
                userLatLng?.let { latLng ->
                    map.addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .title("Mi ubicación")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    )
                }
                val focusLocation = userLatLng ?: state.hotspots.firstOrNull()?.let {
                    LatLng(it.latitude, it.longitude)
                }
                focusLocation?.let { focus ->
                    val zoom = if (userLatLng != null) 15f else 13.5f
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(focus, zoom))
                }
                map.setOnMarkerClickListener { marker ->
                    val hotspotId = marker.tag as? Long
                    val hotspot = state.hotspots.firstOrNull { it.id == hotspotId }
                    viewModel.selectHotspot(hotspot)
                    false
                }
            }
        })

        Column(modifier = Modifier.align(Alignment.TopCenter).padding(16.dp)) {
            if (state.isLoading) {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(12.dp)) {
                        CircularProgressIndicator(modifier = Modifier.padding(end = 12.dp))
                        Text("Cargando mapa...")
                    }
                }
            }
            state.errorMessage?.let { error ->
                Card(
                    modifier = Modifier.padding(top = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                ) {
                    Text(
                        text = error,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            if (state.hotspots.isEmpty()) {
                Card(
                    modifier = Modifier.padding(top = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                ) {
                    Text("No hay hotspots configurados", modifier = Modifier.padding(12.dp))
                }
            }
            if (permissionState != PermissionState.GRANTED) {
                Card(
                    modifier = Modifier.padding(top = 8.dp),
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
        }

        FloatingActionButton(
            onClick = {
                if (permissionState == PermissionState.GRANTED) {
                    requestLocation(fusedClient) { latLng ->
                        viewModel.setUserLocation(latLng.latitude, latLng.longitude)
                    }
                } else {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
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
    Button(
        onClick = {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        },
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text("Abrir ajustes")
    }
}

@SuppressLint("MissingPermission")
private fun requestLocation(client: FusedLocationProviderClient, onLocation: (LatLng) -> Unit) {
    client.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null)
        .addOnSuccessListener { loc ->
            if (loc != null) {
                onLocation(LatLng(loc.latitude, loc.longitude))
            } else {
                client.lastLocation.addOnSuccessListener { last ->
                    last?.let { onLocation(LatLng(it.latitude, it.longitude)) }
                }
            }
        }
}

private fun drawHotspots(hotspots: List<Hotspot>, map: com.google.android.gms.maps.GoogleMap) {
    hotspots.forEach { hotspot ->
        val color = if (hotspot.severity.uppercase() == "CRITICAL") {
            BitmapDescriptorFactory.HUE_RED
        } else {
            BitmapDescriptorFactory.HUE_ORANGE
        }
        map.addMarker(
            MarkerOptions()
                .position(LatLng(hotspot.latitude, hotspot.longitude))
                .title("PM2.5 ${hotspot.pm25}")
                .icon(BitmapDescriptorFactory.defaultMarker(color))
        )?.tag = hotspot.id
    }
}

private fun Context.findActivity(): Activity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is Activity) return currentContext
        currentContext = currentContext.baseContext
    }
    return null
}
