package com.airguardnet.mobile.core.utils

import androidx.compose.ui.graphics.Color
import com.airguardnet.mobile.ui.theme.AlertCritical
import com.airguardnet.mobile.ui.theme.AlertHigh
import com.airguardnet.mobile.ui.theme.AlertLow
import com.airguardnet.mobile.ui.theme.AlertMedium

private const val PM25_MAX_THRESHOLD = 150.0

enum class RiskBand(val label: String, val color: Color) {
    LOW("BAJO", AlertLow),
    MEDIUM("MEDIO", AlertMedium),
    HIGH("ALTO", AlertHigh),
    VERY_HIGH("MUY ALTO", AlertCritical)
}

fun resolveRiskBand(pm25: Double?): RiskBand {
    val value = pm25 ?: return RiskBand.LOW
    return when {
        value < 35 -> RiskBand.LOW
        value < 75 -> RiskBand.MEDIUM
        value < 150 -> RiskBand.HIGH
        else -> RiskBand.VERY_HIGH
    }
}

fun qualityPercent(pm25: Double?): Int {
    val value = pm25 ?: return 0
    val normalized = 1 - (value / PM25_MAX_THRESHOLD)
    return (normalized.coerceIn(0.0, 1.0) * 100).toInt()
}
