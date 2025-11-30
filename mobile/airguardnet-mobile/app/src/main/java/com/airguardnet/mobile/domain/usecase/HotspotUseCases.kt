package com.airguardnet.mobile.domain.usecase

import com.airguardnet.mobile.domain.model.Hotspot
import com.airguardnet.mobile.domain.repository.HotspotRepository

class ObserveHotspotsUseCase(private val repository: HotspotRepository) {
    operator fun invoke() = repository.observeHotspots()
}

class AddHotspotUseCase(private val repository: HotspotRepository) {
    suspend operator fun invoke(hotspot: Hotspot) = repository.addHotspot(hotspot)
}

class ClearHotspotsUseCase(private val repository: HotspotRepository) {
    suspend operator fun invoke() = repository.clear()
}
