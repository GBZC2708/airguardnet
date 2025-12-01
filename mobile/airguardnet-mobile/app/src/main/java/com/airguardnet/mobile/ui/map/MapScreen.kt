package com.airguardnet.mobile.ui.map

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

@SuppressLint("MissingPermission")
@Composable
fun MapScreen(viewModel: MapViewModel = hiltViewModel()) {
    val hotspots by viewModel.hotspots.collectAsState()
    val context = LocalContext.current
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    val fusedClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            fusedClient.lastLocation.addOnSuccessListener { loc ->
                loc?.let { userLocation = LatLng(it.latitude, it.longitude) }
            }
        }
    }

    LaunchedEffect(Unit) { permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { context ->
            MapView(context).apply {
                onCreate(null)
                onResume()
                getMapAsync { map ->
                    map.uiSettings.isZoomControlsEnabled = true
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
                    focusLocation?.let {
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 13f))
                    }
                }
            }
        }, update = { mapView ->
            mapView.getMapAsync { map ->
                map.clear()
                val focusLocation = userLocation ?: hotspots.firstOrNull()?.let { LatLng(it.latitude, it.longitude) }
                hotspots.forEach { hotspot ->
                    val color = if (hotspot.severity.uppercase() == "CRITICAL") BitmapDescriptorFactory.HUE_RED else BitmapDescriptorFactory.HUE_ORANGE
                    map.addMarker(
                        MarkerOptions().position(LatLng(hotspot.latitude, hotspot.longitude)).title("PM2.5 ${hotspot.pm25}")
                            .icon(BitmapDescriptorFactory.defaultMarker(color))
                    )
                }
                focusLocation?.let { map.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 13f)) }
            }
        })
    }
}
