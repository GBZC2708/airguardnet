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
import com.airguardnet.mobile.domain.repository.DeviceRepository
import com.airguardnet.mobile.domain.usecase.ObserveSessionUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class AlertPollingWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val deviceRepository: DeviceRepository,
    private val observeSessionUseCase: ObserveSessionUseCase
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val session = observeSessionUseCase().first() ?: return Result.success()
        val devices = deviceRepository.observeDevices().first()
        val device = devices.firstOrNull { it.assignedUserId == session.userId } ?: return Result.success()
        deviceRepository.refreshAlerts(device.id)
        val alerts = deviceRepository.observeAlerts(device.id).first()
        if (alerts.isNotEmpty()) {
            val latest = alerts.first()
            showNotification(latest.message, latest.severity)
        }
        return Result.success()
    }

    private fun showNotification(message: String, severity: String) {
        val channelId = if (severity.uppercase() == "CRITICAL") "critical_alerts" else "high_alerts"
        val manager = NotificationManagerCompat.from(applicationContext)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_HIGH)
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
