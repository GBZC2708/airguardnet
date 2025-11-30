package com.airguardnet.mobile.domain.usecase

import com.airguardnet.mobile.domain.repository.ConfigRepository

class ObserveSensorConfigsUseCase(private val repository: ConfigRepository) {
    operator fun invoke() = repository.sensorConfigs
}

class RefreshSensorConfigsUseCase(private val repository: ConfigRepository) {
    suspend operator fun invoke() = repository.refreshSensorConfigs()
}
