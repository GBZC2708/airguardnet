package com.airguardnet.user.domain.service;

import java.util.List;

public class ServiceHealthAggregator {

    public List<String> buildServiceList() {
        return List.of("api-gateway", "user-service", "device-service", "notification-service");
    }
}
