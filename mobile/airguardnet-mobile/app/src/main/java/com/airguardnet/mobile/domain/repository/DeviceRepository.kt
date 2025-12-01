package com.airguardnet.mobile.domain.repository

import com.airguardnet.mobile.domain.model.Alert
import com.airguardnet.mobile.domain.model.Device
import com.airguardnet.mobile.domain.model.Reading
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {
    fun observeDevices(): Flow<List<Device>>
    fun observeReadings(deviceId: Long): Flow<List<Reading>>
    fun observeAlerts(deviceId: Long): Flow<List<Alert>>
    suspend fun refreshDevices()
    suspend fun refreshReadings(deviceId: Long)
    suspend fun refreshAlerts(deviceId: Long)
    suspend fun getCachedAlerts(): List<Alert>
}
