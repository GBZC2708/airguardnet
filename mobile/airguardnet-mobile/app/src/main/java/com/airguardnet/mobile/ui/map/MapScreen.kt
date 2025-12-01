package com.airguardnet.mobile.ui.map

import android.Manifest
import android.annotation.SuppressLint
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

@SuppressLint("MissingPermission")
@Composable
fun MapScreen(viewModel: MapViewModel = hiltViewModel()) {
    val hotspots by viewModel.hotspots.collectAsState()
    val context = LocalContext.current
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var selectedHotspot by remember { mutableStateOf<Hotspot?>(null) }
    var hasLocationPermission by remember { mutableStateOf(false) }
    var permissionRequested by remember { mutableStateOf(false) }
    val fusedClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { granted ->
        permissionRequested = true
        hasLocationPermission = granted[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            granted[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (hasLocationPermission) {
            requestLocation(fusedClient) { userLocation = it }
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
    }

    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { context ->
            MapView(context).apply {
                onCreate(null)
                onResume()
                getMapAsync { map ->
                    map.uiSettings.isZoomControlsEnabled = true
                    map.uiSettings.isMyLocationButtonEnabled = false
                    map.isMyLocationEnabled = hasLocationPermission
                    val focusLocation = userLocation ?: hotspots.firstOrNull()?.let { LatLng(it.latitude, it.longitude) }
                    hotspots.forEach { hotspot ->
                        val color = if (hotspot.severity.uppercase() == "CRITICAL") BitmapDescriptorFactory.HUE_RED else BitmapDescriptorFactory.HUE_ORANGE
                        map.addMarker(
                            MarkerOptions()
                                .position(LatLng(hotspot.latitude, hotspot.longitude))
                                .title("PM2.5 ${hotspot.pm25}")
                                .icon(BitmapDescriptorFactory.defaultMarker(color))
                        )
                    }
                    userLocation?.let {
                        map.addMarker(
                            MarkerOptions()
                                .position(it)
                                .title("Mi ubicación")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        )
                    }
                    map.setOnMarkerClickListener { marker ->
                        val hotspot = hotspots.firstOrNull { it.latitude == marker.position.latitude && it.longitude == marker.position.longitude }
                        selectedHotspot = hotspot
                        false
                    }
                    focusLocation?.let {
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 13f))
                    }
                }
            }
        }, update = { mapView ->
            mapView.getMapAsync { map ->
                map.clear()
                map.isMyLocationEnabled = hasLocationPermission
                val focusLocation = userLocation ?: hotspots.firstOrNull()?.let { LatLng(it.latitude, it.longitude) }
                hotspots.forEach { hotspot ->
                    val color = if (hotspot.severity.uppercase() == "CRITICAL") BitmapDescriptorFactory.HUE_RED else BitmapDescriptorFactory.HUE_ORANGE
                    map.addMarker(
                        MarkerOptions().position(LatLng(hotspot.latitude, hotspot.longitude)).title("PM2.5 ${hotspot.pm25}")
                            .icon(BitmapDescriptorFactory.defaultMarker(color))
                    )
                }
                userLocation?.let {
                    map.addMarker(
                        MarkerOptions().position(it).title("Mi ubicación")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    )
                }
                map.setOnMarkerClickListener { marker ->
                    val hotspot = hotspots.firstOrNull { it.latitude == marker.position.latitude && it.longitude == marker.position.longitude }
                    selectedHotspot = hotspot
                    false
                }
                focusLocation?.let { map.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 13f)) }
            }
        })

        if (!hasLocationPermission) {
            Card(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
            ) {
                Text(
                    text = if (permissionRequested) {
                        "Habilita la ubicación para centrar el mapa en tu posición."
                    } else {
                        "Solicitando permisos de ubicación..."
                    },
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        FloatingActionButton(
            onClick = {
                if (hasLocationPermission) {
                    requestLocation(fusedClient) { userLocation = it }
                } else {
                    permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Rounded.MyLocation, contentDescription = "Mi ubicación")
        }

        selectedHotspot?.let { hotspot ->
            Card(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Hotspot", style = MaterialTheme.typography.titleMedium)
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

@SuppressLint("MissingPermission")
private fun requestLocation(client: com.google.android.gms.location.FusedLocationProviderClient, onLocation: (LatLng) -> Unit) {
    client.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null)
        .addOnSuccessListener { loc ->
            loc?.let { onLocation(LatLng(it.latitude, it.longitude)) }
        }
}
