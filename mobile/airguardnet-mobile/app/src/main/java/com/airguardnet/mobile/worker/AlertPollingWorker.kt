package com.airguardnet.mobile.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.airguardnet.mobile.core.notifications.NotificationHelper
import com.airguardnet.mobile.core.preferences.UserPreferencesManager
import com.airguardnet.mobile.domain.model.Device
import com.airguardnet.mobile.domain.repository.DeviceRepository
import com.airguardnet.mobile.domain.usecase.ObserveDevicesUseCase
import com.airguardnet.mobile.domain.usecase.ObserveSessionUseCase
import com.airguardnet.mobile.domain.usecase.RefreshAlertsUseCase
import com.airguardnet.mobile.domain.usecase.RefreshDevicesUseCase
import kotlinx.coroutines.coroutineScope
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
    private val observeDevicesUseCase: ObserveDevicesUseCase,
    private val refreshDevicesUseCase: RefreshDevicesUseCase,
    private val refreshAlertsUseCase: RefreshAlertsUseCase,
    private val preferencesManager: UserPreferencesManager
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result = runCatching {
        val prefs = preferencesManager.preferences.firstOrNull()
        if (prefs != null && !prefs.criticalNotificationsEnabled) return Result.success()

        val session = observeSessionUseCase().firstOrNull() ?: return Result.success()
        val device = resolvePrimaryDevice(prefs?.primaryDeviceId, session.userId) ?: return Result.success()

        refreshAlertsUseCase(device.id)
        val alerts = deviceRepository.getCachedAlerts().filter { it.deviceId == device.id }
        val lastNotified = prefs?.lastNotifiedCriticalAlertAt ?: 0L
        val newCriticals = alerts.filter { alert ->
            alert.severity.equals("CRITICAL", true) && alert.createdAt > lastNotified
        }

        if (newCriticals.isNotEmpty()) {
            val latestTimestamp = newCriticals.maxOf { it.createdAt }
            val preview = newCriticals.joinToString(limit = 3, separator = " • ") { it.message }
            NotificationHelper.showCriticalAlertNotification(
                context = applicationContext,
                newCount = newCriticals.size,
                deviceUid = device.deviceUid,
                message = preview
            )
            preferencesManager.setLastNotifiedCriticalAlertAt(latestTimestamp)
        }
        Result.success()
    }.getOrElse { Result.retry() }

    private suspend fun resolvePrimaryDevice(primaryId: Long?, userId: Long): Device? = coroutineScope {
        var devices = observeDevicesUseCase().first()
        if (devices.isEmpty()) {
            refreshDevicesUseCase()
            devices = observeDevicesUseCase().first()
        }

        primaryId?.let { id -> devices.firstOrNull { it.id == id } }
            ?: devices.firstOrNull { it.assignedUserId == userId }
            ?: devices.firstOrNull()
    }
}
