package com.airguardnet.mobile.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.airguardnet.mobile.R
import com.airguardnet.mobile.core.preferences.UserPreferencesManager
import com.airguardnet.mobile.domain.repository.DeviceRepository
import com.airguardnet.mobile.domain.usecase.ObserveSessionUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

@HiltWorker
class AlertPollingWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val deviceRepository: DeviceRepository,
    private val observeSessionUseCase: ObserveSessionUseCase,
    private val preferencesManager: UserPreferencesManager
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result = runCatching {
        val prefs = preferencesManager.preferences.firstOrNull()
        if (prefs != null && !prefs.criticalNotificationsEnabled) return Result.success()
        val session = observeSessionUseCase().first() ?: return Result.success()
        var devices = deviceRepository.observeDevices().first()
        if (devices.isEmpty()) {
            deviceRepository.refreshDevices()
            devices = deviceRepository.observeDevices().first()
        }
        val device = prefs?.primaryDeviceId?.let { id -> devices.firstOrNull { it.id == id } }
            ?: devices.firstOrNull { it.assignedUserId == session.userId }
            ?: devices.firstOrNull()
            ?: return Result.success()

        val previousIds = deviceRepository.getCachedAlerts().map { it.id }.toSet()
        deviceRepository.refreshAlerts(device.id)
        val alerts = deviceRepository.getCachedAlerts()
        val newCriticals = alerts.filter { alert ->
            alert.id !in previousIds &&
                (alert.severity.equals("HIGH", true) || alert.severity.equals("CRITICAL", true))
        }
        newCriticals.forEach { alert ->
            showNotification(alert.message, alert.severity)
        }
        Result.success()
    }.getOrElse { Result.retry() }

    private fun showNotification(message: String, severity: String) {
        val channelId = "NOTIF_CHANNEL_ALERTS"
        val manager = NotificationManagerCompat.from(applicationContext)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Alertas AirGuardNet", NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
        }
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Alerta $severity de polvo")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        manager.notify(severity.hashCode(), notification)
    }
}
