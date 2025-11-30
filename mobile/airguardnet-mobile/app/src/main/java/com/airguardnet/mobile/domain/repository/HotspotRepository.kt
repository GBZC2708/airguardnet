package com.airguardnet.mobile.domain.repository

import com.airguardnet.mobile.domain.model.Hotspot
import kotlinx.coroutines.flow.Flow

interface HotspotRepository {
    fun observeHotspots(): Flow<List<Hotspot>>
    suspend fun addHotspot(hotspot: Hotspot)
    suspend fun clear()
}
