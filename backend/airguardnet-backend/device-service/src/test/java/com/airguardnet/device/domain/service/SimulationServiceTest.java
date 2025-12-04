package com.airguardnet.device.domain.service;

import com.airguardnet.device.domain.model.SimulatedPoint;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimulationServiceTest {

    private final SimulationService simulationService = new SimulationService();

    @Test
    // Nro 68: Validar que servicio de simulación genere serie de datos con longitud solicitada
    void generatePeakScenario_returnsRequestedSize() {
        List<SimulatedPoint> points = simulationService.generatePeakScenario(50);

        assertEquals(50, points.size());
    }

    @Test
    // Nro 69: Validar que la simulación nunca genere airQualityPercent fuera de 0–100
    void generateNormalScenario_valuesWithinRange() {
        List<SimulatedPoint> points = simulationService.generateNormalScenario(100);

        assertTrue(points.stream().allMatch(p -> p.getAirQualityPercent() >= 0 && p.getAirQualityPercent() <= 100));
    }
}
