package com.airguardnet.mobile.ui.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.airguardnet.mobile.MainActivity
import com.airguardnet.mobile.R
import com.airguardnet.mobile.data.local.AirGuardNetDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
            val db = AirGuardNetDatabase.getInstance(context)
            val session = db.userSessionDao().getSession()
            val devices = db.deviceDao().getAll()
            val device = devices.firstOrNull { it.assignedUserId == session?.userId } ?: devices.firstOrNull()
            val reading = device?.let { db.readingDao().getLatest(it.id) }
            val alerts = db.alertDao().getAll().filter { it.deviceId == device?.id }
            withContext(Dispatchers.Main) {
                views.setTextViewText(R.id.widgetAlerts, "PM2.5: ${reading?.pm25 ?: "--"}")
                views.setTextViewText(R.id.widgetTiempo, "Calidad: ${reading?.airQualityPercent ?: "--"}% | Alertas: ${alerts.size}")
                appWidgetManager.updateAppWidget(widgetId, views)
            }
        }
    }
}
