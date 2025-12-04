// Cobertura matriz Nro 53–54 y 64–65
package com.airguardnet.user.domain.service;

import com.airguardnet.user.domain.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthorizationServiceTest {

    private final AuthorizationService authorizationService = new AuthorizationService();

    @Test
    // Nro 53: Validar que solo usuarios ADMIN pueden crear nuevos usuarios
    void canCreateUser_admin_true() {
        User admin = User.builder().role("ADMIN").build();

        assertTrue(authorizationService.canCreateUser(admin));
    }

    @Test
    // Nro 54: Validar que SUPERVISOR no pueda crear usuarios
    void canCreateUser_supervisor_false() {
        User supervisor = User.builder().role("SUPERVISOR").build();

        assertFalse(authorizationService.canCreateUser(supervisor));
    }

    @Test
    // Nro 64: Validar que solo admin pueda ver reportes agregados globales
    void canViewGlobalReports_admin_true() {
        User admin = User.builder().role("ADMIN").build();

        assertTrue(authorizationService.canViewGlobalReports(admin));
    }

    @Test
    // Nro 65: Validar que técnico no pueda ver reportes globales
    void canViewGlobalReports_tecnico_false() {
        User tecnico = User.builder().role("TECNICO").build();

        assertFalse(authorizationService.canViewGlobalReports(tecnico));
    }
}
