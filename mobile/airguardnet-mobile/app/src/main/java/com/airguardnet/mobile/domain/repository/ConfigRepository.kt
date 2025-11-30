package com.airguardnet.mobile.domain.repository

import com.airguardnet.mobile.domain.model.SensorConfig
import kotlinx.coroutines.flow.Flow

interface ConfigRepository {
    val sensorConfigs: Flow<List<SensorConfig>>
    suspend fun refreshSensorConfigs()
}
