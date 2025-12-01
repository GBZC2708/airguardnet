package com.airguardnet.mobile.ui.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.compose.ui.graphics.toArgb
import com.airguardnet.mobile.MainActivity
import com.airguardnet.mobile.R
import com.airguardnet.mobile.core.utils.qualityPercent
import com.airguardnet.mobile.core.utils.resolveRiskBand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ResumenWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        appWidgetIds.forEach { appWidgetId ->
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, widgetId: Int) {
        val intent = Intent(context, MainActivity::class.java)
        val pending = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val views = RemoteViews(context.packageName, R.layout.widget_resumen)
        views.setOnClickPendingIntent(R.id.widgetAlerts, pending)
        views.setOnClickPendingIntent(R.id.widgetTiempo, pending)

        CoroutineScope(Dispatchers.IO).launch {
            val entryPoint = context.widgetEntryPoint()
            val prefs = entryPoint.preferences().preferences.firstOrNull()
            val session = entryPoint.session().invoke().firstOrNull()
            if (session == null) {
                withContext(Dispatchers.Main) {
                    views.setTextViewText(R.id.widgetAlerts, "Inicia sesión para ver tu resumen")
                    views.setTextViewText(R.id.widgetTiempo, "Alertas críticas 24h: --")
                    appWidgetManager.updateAppWidget(widgetId, views)
                }
                return@launch
            }

            entryPoint.refreshDevices().invoke()
            val devices = entryPoint.devices().invoke().first()
            val device = prefs?.primaryDeviceId?.let { id -> devices.firstOrNull { it.id == id } }
                ?: devices.firstOrNull { it.assignedUserId == session.userId }
                ?: devices.firstOrNull()

            if (device == null) {
                withContext(Dispatchers.Main) {
                    views.setTextViewText(R.id.widgetAlerts, "Sin dispositivo asignado")
                    views.setTextViewText(R.id.widgetTiempo, "Alertas críticas 24h: --")
                    appWidgetManager.updateAppWidget(widgetId, views)
                }
                return@launch
            }

            entryPoint.refreshReadings().invoke(device.id)
            entryPoint.refreshAlerts().invoke(device.id)
            val reading = entryPoint.readings().invoke(device.id).firstOrNull()?.firstOrNull()
            val alerts = entryPoint.alerts().invoke(device.id).firstOrNull().orEmpty()
            val critical24h = alerts.count {
                it.severity.equals("CRITICAL", true) &&
                        it.createdAt > System.currentTimeMillis() - 86_400_000
            }
            val band = resolveRiskBand(reading?.pm25)
            val quality = qualityPercent(reading?.pm25)

            withContext(Dispatchers.Main) {
                views.setTextViewText(
                    R.id.widgetAlerts,
                    "${session.name} | PM2.5: ${reading?.pm25 ?: "--"} µg/m³"
                )
                views.setTextViewText(
                    R.id.widgetTiempo,
                    "Calidad: ${if (reading == null) "--" else quality.toString()}% · Críticas 24h: $critical24h"
                )
                views.setInt(R.id.widgetRoot, "setBackgroundColor", band.color.toArgb())
                appWidgetManager.updateAppWidget(widgetId, views)
            }
        }
    }
}
