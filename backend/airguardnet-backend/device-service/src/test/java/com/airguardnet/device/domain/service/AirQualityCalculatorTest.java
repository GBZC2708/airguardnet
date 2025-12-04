package com.airguardnet.device.domain.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AirQualityCalculatorTest {

    private final AirQualityCalculator calculator = new AirQualityCalculator();

    @Test
    // Nro 24: Calcular air_quality_percent como 100 - risk_index
    void calculateAirQualityPercent_risk40_returns60() {
        assertEquals(60, calculator.calculateAirQualityPercent(40));
    }

    @Test
    // Nro 25: Clamp de air_quality_percent negativo a 0
    void calculateAirQualityPercent_risk150_returns0() {
        assertEquals(0, calculator.calculateAirQualityPercent(150));
    }
}
