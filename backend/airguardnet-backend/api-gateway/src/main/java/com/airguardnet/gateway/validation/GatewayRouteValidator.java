package com.airguardnet.gateway.validation;

import java.util.List;

public class GatewayRouteValidator {

    private final List<String> allowedRoutes;

    public GatewayRouteValidator(List<String> allowedRoutes) {
        this.allowedRoutes = allowedRoutes;
    }

    public boolean isValidRoute(String path) {
        return allowedRoutes.stream().anyMatch(path::startsWith);
    }
}
