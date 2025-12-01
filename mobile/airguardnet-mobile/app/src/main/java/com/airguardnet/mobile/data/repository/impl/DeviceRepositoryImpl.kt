package com.airguardnet.mobile.data.repository.impl

import com.airguardnet.mobile.data.local.dao.AlertDao
import com.airguardnet.mobile.data.local.dao.DeviceDao
import com.airguardnet.mobile.data.local.dao.ReadingDao
import com.airguardnet.mobile.data.mapper.toDomain
import com.airguardnet.mobile.data.mapper.toEntity
import com.airguardnet.mobile.data.remote.AirGuardNetApiService
import com.airguardnet.mobile.domain.model.Alert
import com.airguardnet.mobile.domain.model.Device
import com.airguardnet.mobile.domain.model.Reading
import com.airguardnet.mobile.domain.repository.DeviceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DeviceRepositoryImpl(
    private val api: AirGuardNetApiService,
    private val deviceDao: DeviceDao,
    private val readingDao: ReadingDao,
    private val alertDao: AlertDao
) : DeviceRepository {
    override fun observeDevices(): Flow<List<Device>> = deviceDao.observeDevices().map { list -> list.map { it.toDomain() } }

    override fun observeReadings(deviceId: Long): Flow<List<Reading>> = readingDao.observeByDevice(deviceId).map { list -> list.map { it.toDomain() } }

    override fun observeAlerts(deviceId: Long): Flow<List<Alert>> = alertDao.observeAll().map { alerts ->
        alerts.filter { it.deviceId == deviceId }.map { alert -> alert.toDomain() }
    }

    override suspend fun refreshDevices() {
        val response = runCatching { api.getDevices() }.getOrNull() ?: return
        if (response.success && response.data != null) {
            deviceDao.clear()
            deviceDao.insertAll(response.data.map { it.toEntity() })
        }
    }

    override suspend fun refreshReadings(deviceId: Long) {
        val response = runCatching { api.getDeviceReadings(deviceId) }.getOrNull() ?: return
        if (response.success && response.data != null) {
            readingDao.insertAll(response.data.map { it.toEntity() })
        }
    }

    override suspend fun refreshAlerts(deviceId: Long) {
        val response = runCatching { api.getAlerts() }.getOrNull() ?: return
        if (response.success && response.data != null) {
            alertDao.clearAll()
            alertDao.insertAll(response.data.map { it.toEntity() })
        }
    }

    override suspend fun getCachedAlerts(): List<Alert> = alertDao.getAll().map { it.toDomain() }
}
