package com.airguardnet.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.airguardnet.mobile.core.design.AirGuardNetTheme
import com.airguardnet.mobile.domain.model.UserSession
import com.airguardnet.mobile.ui.alerts.AlertsScreen
import com.airguardnet.mobile.ui.auth.AuthScreen
import com.airguardnet.mobile.ui.history.HistoryScreen
import com.airguardnet.mobile.ui.home.HomeScreen
import com.airguardnet.mobile.ui.map.MapScreen
import com.airguardnet.mobile.ui.navigation.BottomNavItem
import com.airguardnet.mobile.ui.navigation.NavRoutes
import com.airguardnet.mobile.ui.navigation.bottomNavItems
import com.airguardnet.mobile.ui.navigation.RootViewModel
import com.airguardnet.mobile.ui.profile.ProfileScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AirGuardNetRoot() }
    }
}

@Composable
fun AirGuardNetRoot(viewModel: RootViewModel = androidx.hilt.navigation.compose.hiltViewModel()) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val sessionState = viewModel.session.collectAsState()
    val session: UserSession? = sessionState.value
    LaunchedEffect(session) {
        if (session != null) {
            navController.navigate(NavRoutes.Home.route) {
                popUpTo(NavRoutes.Login.route) { inclusive = true }
                launchSingleTop = true
            }
        } else if (currentRoute != NavRoutes.Login.route) {
            navController.navigate(NavRoutes.Login.route) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }
    AirGuardNetTheme {
        Scaffold(
            bottomBar = {
                if (currentRoute != null && currentRoute != NavRoutes.Login.route) {
                    NavigationBar {
                        bottomNavItems.forEach { item ->
                            NavigationBarItem(
                                selected = currentRoute == item.route.route,
                                onClick = {
                                    navController.navigate(item.route.route) {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = { Icon(item.icon, contentDescription = item.label) },
                                label = { Text(item.label) }
                            )
                        }
                    }
                }
            }
        ) { padding ->
            NavHost(navController = navController, startDestination = NavRoutes.Login.route, modifier = Modifier.padding(padding)) {
                composable(NavRoutes.Login.route) { AuthScreen(onLoggedIn = { navController.navigate(NavRoutes.Home.route) { popUpTo(NavRoutes.Login.route) { inclusive = true } } }) }
                composable(NavRoutes.Home.route) { HomeScreen(onHistory = { navController.navigate(NavRoutes.History.route) }) }
                composable(NavRoutes.History.route) { HistoryScreen() }
                composable(NavRoutes.Alerts.route) { AlertsScreen() }
                composable(NavRoutes.Map.route) { MapScreen() }
                composable(NavRoutes.Profile.route) { ProfileScreen(onLoggedOut = { navController.navigate(NavRoutes.Login.route) { popUpTo(0) { inclusive = true } } }) }
            }
        }
    }
}
