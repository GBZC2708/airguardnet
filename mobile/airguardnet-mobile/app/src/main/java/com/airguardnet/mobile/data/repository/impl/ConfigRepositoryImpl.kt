package com.airguardnet.mobile.data.repository.impl

import com.airguardnet.mobile.data.remote.AirGuardNetApiService
import com.airguardnet.mobile.data.mapper.toDomain
import com.airguardnet.mobile.domain.model.SensorConfig
import com.airguardnet.mobile.domain.repository.ConfigRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ConfigRepositoryImpl(private val api: AirGuardNetApiService) : ConfigRepository {
    private val _sensorConfigs = MutableStateFlow<List<SensorConfig>>(emptyList())
    override val sensorConfigs: Flow<List<SensorConfig>> = _sensorConfigs.asStateFlow()

    override suspend fun refreshSensorConfigs() {
        val response = api.getSensorConfigs()
        if (response.success && response.data != null) {
            _sensorConfigs.value = response.data.map { it.toDomain() }
        }
    }
}
