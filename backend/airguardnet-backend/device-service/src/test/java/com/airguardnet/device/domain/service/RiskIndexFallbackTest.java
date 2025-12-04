package com.airguardnet.device.domain.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RiskIndexFallbackTest {

    @Test
    // Nro 23: Fallback risk_index BAJO cuando no hay config PM25
    void calculateFallback_lowPm_returns10() {
        RiskIndexFallback fallback = new RiskIndexFallback();
        assertEquals(10, fallback.calculateFallback(20));
    }
}
