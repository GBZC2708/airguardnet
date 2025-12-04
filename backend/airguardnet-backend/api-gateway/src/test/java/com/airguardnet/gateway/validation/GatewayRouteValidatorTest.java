package com.airguardnet.gateway.validation;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GatewayRouteValidatorTest {

    @Test
    // Nro 45: Validar que endpoint request desconocido se marque como inválido en gateway
    void isValidRoute_unknown_false() {
        GatewayRouteValidator validator = new GatewayRouteValidator(List.of("/api/login", "/api/users", "/api/devices"));

        assertFalse(validator.isValidRoute("/api/ruta-que-no-existe"));
    }

    @Test
    // Nro 46: Validar que /api/login es ruta válida
    void isValidRoute_login_true() {
        GatewayRouteValidator validator = new GatewayRouteValidator(List.of("/api/login", "/api/users"));

        assertTrue(validator.isValidRoute("/api/login"));
    }
}
