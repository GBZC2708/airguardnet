package com.airguardnet.mobile.data.repository.impl

import com.airguardnet.mobile.data.local.dao.HotspotDao
import com.airguardnet.mobile.data.mapper.toDomain
import com.airguardnet.mobile.data.mapper.toEntity
import com.airguardnet.mobile.domain.model.Hotspot
import com.airguardnet.mobile.domain.repository.HotspotRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HotspotRepositoryImpl(private val hotspotDao: HotspotDao) : HotspotRepository {
    override fun observeHotspots(): Flow<List<Hotspot>> = hotspotDao.observeHotspots().map { it.map { entity -> entity.toDomain() } }

    override suspend fun addHotspot(hotspot: Hotspot) {
        hotspotDao.insert(hotspot.toEntity())
    }

    override suspend fun clear() {
        hotspotDao.clear()
    }
}
