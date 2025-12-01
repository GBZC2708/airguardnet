package com.airguardnet.mobile.ui.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.widget.RemoteViews
import com.airguardnet.mobile.MainActivity
import com.airguardnet.mobile.R
import com.airguardnet.mobile.data.local.AirGuardNetDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SemaforoWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        appWidgetIds.forEach { appWidgetId ->
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, widgetId: Int) {
        val intent = Intent(context, MainActivity::class.java)
        val pending = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val views = RemoteViews(context.packageName, R.layout.widget_semaforo)
        views.setOnClickPendingIntent(R.id.widgetState, pending)
        views.setOnClickPendingIntent(R.id.widgetPm25, pending)

        CoroutineScope(Dispatchers.IO).launch {
            val db = AirGuardNetDatabase.getInstance(context)
            val session = db.userSessionDao().getSession()
            val devices = db.deviceDao().getAll()
            val device = devices.firstOrNull { it.assignedUserId == session?.userId } ?: devices.firstOrNull()
            val reading = device?.let { db.readingDao().getLatest(it.id) }
            val (label, color) = when (reading?.riskIndex ?: 0) {
                in 0..40 -> "Buena" to Color.parseColor("#4CAF50")
                in 41..70 -> "Moderada" to Color.parseColor("#FFC107")
                in 71..90 -> "Mala" to Color.parseColor("#FF7043")
                else -> "Muy mala" to Color.parseColor("#F44336")
            }
            withContext(Dispatchers.Main) {
                views.setTextViewText(R.id.widgetState, label)
                views.setTextViewText(R.id.widgetPm25, "PM2.5: ${reading?.pm25 ?: "--"}")
                views.setInt(R.id.widgetRoot, "setBackgroundColor", color)
                appWidgetManager.updateAppWidget(widgetId, views)
            }
        }
    }
}
