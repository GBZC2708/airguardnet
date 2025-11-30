package com.airguardnet.mobile.domain.usecase

import com.airguardnet.mobile.domain.repository.DeviceRepository

class ObserveDevicesUseCase(private val repository: DeviceRepository) {
    operator fun invoke() = repository.observeDevices()
}

class ObserveReadingsUseCase(private val repository: DeviceRepository) {
    operator fun invoke(deviceId: Long) = repository.observeReadings(deviceId)
}

class ObserveAlertsUseCase(private val repository: DeviceRepository) {
    operator fun invoke(deviceId: Long) = repository.observeAlerts(deviceId)
}

class RefreshDevicesUseCase(private val repository: DeviceRepository) {
    suspend operator fun invoke() = repository.refreshDevices()
}

class RefreshReadingsUseCase(private val repository: DeviceRepository) {
    suspend operator fun invoke(deviceId: Long) = repository.refreshReadings(deviceId)
}

class RefreshAlertsUseCase(private val repository: DeviceRepository) {
    suspend operator fun invoke(deviceId: Long) = repository.refreshAlerts(deviceId)
}
