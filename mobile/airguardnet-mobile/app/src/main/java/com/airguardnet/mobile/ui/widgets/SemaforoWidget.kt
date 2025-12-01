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
import com.airguardnet.mobile.core.preferences.UserPreferencesManager
import com.airguardnet.mobile.core.utils.resolveRiskBand
import com.airguardnet.mobile.domain.usecase.ObserveAlertsUseCase
import com.airguardnet.mobile.domain.usecase.ObserveDevicesUseCase
import com.airguardnet.mobile.domain.usecase.ObserveReadingsUseCase
import com.airguardnet.mobile.domain.usecase.ObserveSessionUseCase
import com.airguardnet.mobile.domain.usecase.RefreshAlertsUseCase
import com.airguardnet.mobile.domain.usecase.RefreshDevicesUseCase
import com.airguardnet.mobile.domain.usecase.RefreshReadingsUseCase
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
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
            val entryPoint = context.widgetEntryPoint()
            val prefs = entryPoint.preferences().preferences.firstOrNull()
            val session = entryPoint.session().invoke().firstOrNull()
            if (session == null) {
                withContext(Dispatchers.Main) {
                    views.setTextViewText(R.id.widgetState, "Inicia sesión en AirGuardNet")
                    views.setTextViewText(R.id.widgetPm25, "PM2.5: --")
                    views.setInt(R.id.widgetRoot, "setBackgroundColor", 0xFF607D8B.toInt())
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
                    views.setTextViewText(R.id.widgetState, "Sin dispositivo")
                    views.setTextViewText(R.id.widgetPm25, "PM2.5: --")
                    appWidgetManager.updateAppWidget(widgetId, views)
                }
                return@launch
            }

            entryPoint.refreshReadings().invoke(device.id)
            val reading = entryPoint.readings().invoke(device.id).firstOrNull()?.firstOrNull()
            val band = resolveRiskBand(reading?.pm25)

            withContext(Dispatchers.Main) {
                views.setTextViewText(R.id.widgetState, band.label)
                views.setTextViewText(R.id.widgetPm25, "PM2.5: ${reading?.pm25 ?: "--"} µg/m³")
                views.setInt(R.id.widgetRoot, "setBackgroundColor", band.color.toArgb())
                appWidgetManager.updateAppWidget(widgetId, views)
            }
        }
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetEntryPoint {
    fun session(): ObserveSessionUseCase
    fun devices(): ObserveDevicesUseCase
    fun readings(): ObserveReadingsUseCase
    fun alerts(): ObserveAlertsUseCase
    fun refreshReadings(): RefreshReadingsUseCase
    fun refreshDevices(): RefreshDevicesUseCase
    fun refreshAlerts(): RefreshAlertsUseCase
    fun preferences(): UserPreferencesManager
}

// OJO: ya no es private, así ResumenWidget también lo puede usar
fun Context.widgetEntryPoint(): WidgetEntryPoint =
    EntryPointAccessors.fromApplication(this, WidgetEntryPoint::class.java)
