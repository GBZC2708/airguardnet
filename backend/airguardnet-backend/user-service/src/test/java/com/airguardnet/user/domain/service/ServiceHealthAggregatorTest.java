package com.airguardnet.user.domain.service;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ServiceHealthAggregatorTest {

    private final ServiceHealthAggregator aggregator = new ServiceHealthAggregator();

    @Test
    // Nro 61: Validar que lista de servicios incluya user-service y device-service
    void buildServiceList_containsKeyServices() {
        List<String> services = aggregator.buildServiceList();

        assertTrue(services.contains("user-service"));
        assertTrue(services.contains("device-service"));
    }
}
