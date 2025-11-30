package com.airguardnet.mobile.ui.map

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

@SuppressLint("MissingPermission")
@Composable
fun MapScreen(viewModel: MapViewModel = hiltViewModel()) {
    val hotspots by viewModel.hotspots.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { context ->
            MapView(context).apply {
                onCreate(null)
                getMapAsync { map ->
                    map.uiSettings.isZoomControlsEnabled = true
                    hotspots.forEach { hotspot ->
                        val color = if (hotspot.severity.uppercase() == "CRITICAL") BitmapDescriptorFactory.HUE_RED else BitmapDescriptorFactory.HUE_ORANGE
                        map.addMarker(
                            MarkerOptions()
                                .position(LatLng(hotspot.latitude, hotspot.longitude))
                                .title("PM2.5 ${hotspot.pm25}")
                                .icon(BitmapDescriptorFactory.defaultMarker(color))
                        )
                    }
                    hotspots.firstOrNull()?.let {
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 13f))
                    }
                }
            }
        }, update = { mapView ->
            mapView.getMapAsync { map ->
                map.clear()
                hotspots.forEach { hotspot ->
                    val color = if (hotspot.severity.uppercase() == "CRITICAL") BitmapDescriptorFactory.HUE_RED else BitmapDescriptorFactory.HUE_ORANGE
                    map.addMarker(
                        MarkerOptions().position(LatLng(hotspot.latitude, hotspot.longitude)).title("PM2.5 ${hotspot.pm25}")
                            .icon(BitmapDescriptorFactory.defaultMarker(color))
                    )
                }
                hotspots.firstOrNull()?.let {
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 13f))
                }
            }
        })
    }
}
