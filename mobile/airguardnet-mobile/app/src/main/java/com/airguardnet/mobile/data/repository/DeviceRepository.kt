package com.airguardnet.mobile.data.repository

import com.airguardnet.mobile.core.network.ApiResponse
import com.airguardnet.mobile.data.remote.AirGuardNetApiService
import com.airguardnet.mobile.data.remote.dto.AlertDto
import com.airguardnet.mobile.data.remote.dto.DeviceDto
import com.airguardnet.mobile.data.remote.dto.ReadingDto

class DeviceRepository(
    private val api: AirGuardNetApiService
) {

    suspend fun getAllDevices(): ApiResponse<List<DeviceDto>> =
        api.getDevices()

    /**
     * Devuelve el dispositivo asignado al usuario con id [userId],
     * o null si no hay ninguno.
     */
    suspend fun getAssignedDeviceForUser(userId: Long): DeviceDto? {
        val response = api.getDevices()
        val devices: List<DeviceDto> = response.data ?: return null
        if (!response.success || devices.isEmpty()) return null
        return devices.firstOrNull { it.assignedUserId == userId }
    }

    suspend fun getDeviceReadings(deviceId: Long): ApiResponse<List<ReadingDto>> =
        api.getDeviceReadings(deviceId)

    suspend fun getDeviceAlerts(deviceId: Long): ApiResponse<List<AlertDto>> =
        api.getDeviceAlerts(deviceId)
}
