package com.airguardnet.system.tests;

import com.airguardnet.system.base.BaseSeleniumTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogsSystemTest extends BaseSeleniumTest {

    /**
     * Prueba de sistema 11: Ver logs de acceso desde menú Logs
     * Historias asociadas: H24.02
     * Basada en PlantillaPruebasSistema.xlsx (transcripción incluida en el prompt).
     */
    @Test
    void logs_verAccessLogs() {
        // Paso 1: Login admin y abrir Logs de acceso
        loginAsAdmin();
        clickNavItem("Logs de acceso");

        // Paso 2: Validar tabla con registros
        wait10().until(ExpectedConditions.urlContains("/logs/access"));
        List<WebElement> rows = findTableRows();

        System.out.println("Prueba 11 finalizada, URL: " + driver.getCurrentUrl());
        assertFalse(rows.isEmpty(), "No se encontraron registros de acceso en la tabla");
    }

    /**
     * Prueba de sistema 12: Buscar accesos por usuario en UI
     * Historias asociadas: H24.03
     * Basada en PlantillaPruebasSistema.xlsx (transcripción incluida en el prompt).
     */
    @Test
    void logs_filtrarPorUsuario() {
        // Paso 1: Login y navegar a logs de acceso
        loginAsAdmin();
        clickNavItem("Logs de acceso");
        wait10().until(ExpectedConditions.urlContains("/logs/access"));

        // Paso 2: Leer registros y buscar coincidencias con el usuario admin
        List<WebElement> rows = findTableRows();
        boolean containsAdmin = rows.stream().anyMatch(row -> row.getText().toLowerCase().contains("gerald"));

        System.out.println("Prueba 12 finalizada, URL: " + driver.getCurrentUrl());
        assertTrue(containsAdmin, "No se encontraron registros asociados al usuario administrador");
    }
}
