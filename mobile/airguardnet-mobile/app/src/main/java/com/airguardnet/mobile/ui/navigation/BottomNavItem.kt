package com.airguardnet.mobile.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Timeline
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: NavRoutes, val icon: ImageVector, val label: String) {
    object HomeItem : BottomNavItem(NavRoutes.Home, Icons.Rounded.Home, "Home")
    object HistoryItem : BottomNavItem(NavRoutes.History, Icons.Rounded.Timeline, "Historial")
    object AlertsItem : BottomNavItem(NavRoutes.Alerts, Icons.Rounded.Notifications, "Alertas")
    object MapItem : BottomNavItem(NavRoutes.Map, Icons.Rounded.Map, "Mapa")
    object ProfileItem : BottomNavItem(NavRoutes.Profile, Icons.Rounded.AccountCircle, "Perfil")
}

val bottomNavItems = listOf(
    BottomNavItem.HomeItem,
    BottomNavItem.HistoryItem,
    BottomNavItem.AlertsItem,
    BottomNavItem.MapItem,
    BottomNavItem.ProfileItem
)
