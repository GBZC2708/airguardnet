package com.airguardnet.device.domain.service;

import com.airguardnet.device.domain.model.SimulatedPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulationService {

    private final Random random = new Random();

    public List<SimulatedPoint> generatePeakScenario(int points) {
        List<SimulatedPoint> list = new ArrayList<>();
        for (int i = 0; i < points; i++) {
            list.add(SimulatedPoint.builder()
                    .airQualityPercent(Math.max(0, 100 - (i % 100)))
                    .pm25(50 + i)
                    .build());
        }
        return list;
    }

    public List<SimulatedPoint> generateNormalScenario(int points) {
        List<SimulatedPoint> list = new ArrayList<>();
        for (int i = 0; i < points; i++) {
            int airQuality = random.nextInt(101);
            list.add(SimulatedPoint.builder()
                    .airQualityPercent(airQuality)
                    .pm25(airQuality)
                    .build());
        }
        return list;
    }
}
