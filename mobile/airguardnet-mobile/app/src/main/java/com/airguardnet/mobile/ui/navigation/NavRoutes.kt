package com.airguardnet.mobile.ui.navigation

enum class NavRoutes(val route: String) {
    Login("login"),
    Home("home"),
    History("history"),
    Alerts("alerts"),
    Map("map"),
    Profile("profile"),
    RealtimeDemo("realtime_demo"),
    RealtimeLive("realtime_live");

    fun withOptionalFilter(filter: String?): String {
        return if (filter.isNullOrBlank()) route else "$route?filter=$filter"
    }
}
