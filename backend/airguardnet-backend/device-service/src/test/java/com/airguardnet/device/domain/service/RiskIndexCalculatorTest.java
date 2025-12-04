// Cobertura matriz Nro 21–22
package com.airguardnet.device.domain.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RiskIndexCalculatorTest {

    private final RiskIndexCalculator calculator = new RiskIndexCalculator();

    @Test
    // Nro 21: Calcular risk_index en tramo intermedio
    void calculate_midRange_returnsBetween26And70() {
        int idx = calculator.calculate(50, 35, 75);
        assertTrue(idx >= 26 && idx <= 70);
    }

    @Test
    // Nro 22: Calcular risk_index en tramo crítico
    void calculate_criticalRange_returnsBetween71And100() {
        int idx = calculator.calculate(120, 35, 75);
        assertTrue(idx >= 71 && idx <= 100);
    }
}
