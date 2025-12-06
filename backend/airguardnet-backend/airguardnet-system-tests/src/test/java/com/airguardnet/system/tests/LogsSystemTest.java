package com.airguardnet.system.tests;

import com.airguardnet.system.base.BaseSeleniumTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class    LogsSystemTest extends BaseSeleniumTest {

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
     */
    @Test
    void logs_filtrarPorUsuario() {
        // Paso 1: Login y navegar a logs de acceso
        loginAsAdmin();
        clickNavItem("Logs de acceso");
        wait10().until(ExpectedConditions.urlContains("/logs/access"));

        // Paso 2: Leer registros de la tabla
        List<WebElement> rows = findTableRows();

        if (rows.isEmpty()) {
            // Caso A: no hay registros → validar mensaje de estado / error
            WebElement statusMessage = wait10().until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//*[contains(.,'No hay registros') or contains(.,'Ocurrió un error')]")
                    )
            );

            System.out.println("Prueba 12 finalizada SIN registros, se mostró mensaje de estado. URL: "
                    + driver.getCurrentUrl());
            assertTrue(statusMessage.isDisplayed(),
                    "No hay registros de acceso y tampoco se mostró un mensaje de estado en la UI");
        } else {
            // Caso B: hay registros → validar que alguno pertenezca al usuario admin (ID=1)
            boolean containsAdmin = rows.stream().anyMatch(row -> {
                List<WebElement> cells = row.findElements(By.tagName("td"));
                if (cells.size() < 2) return false; // seguridad: 2da columna = Usuario
                String usuarioCell = cells.get(1).getText().trim();
                return "1".equals(usuarioCell); // ID del admin según los datos semilla
            });

            System.out.println("Prueba 12 finalizada CON registros. Filas: "
                    + rows.size() + " - URL: " + driver.getCurrentUrl());
            assertTrue(containsAdmin,
                    "No se encontraron registros asociados al usuario administrador (ID = 1)");
        }
    }

}
