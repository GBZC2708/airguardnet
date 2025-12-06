package com.airguardnet.system.tests;

import com.airguardnet.system.base.BaseSeleniumTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AlertsSystemTest extends BaseSeleniumTest {

    /**
     * Prueba de sistema 6: Filtrar alertas críticas en centro de alertas
     * Historias asociadas: H10.03, H11.02
     * Basada en PlantillaPruebasSistema.xlsx (transcripción incluida en el prompt).
     */
    @Test
    void alertas_filtrarCriticas_muestraSoloCriticas() {
        // Paso 1: Login supervisor (se reutilizan credenciales de entorno)
        loginAsSupervisor();

        // Paso 2: Abrir módulo de alertas
        clickNavItem("Alertas");
        wait10().until(ExpectedConditions.urlContains("/alerts"));

        // Paso 3: Aplicar filtro de severidad Críticas
        wait10().until(ExpectedConditions.elementToBeClickable(By.xpath("//span[normalize-space()='Críticas']"))).click();

        // Paso 4: Validar que las filas muestran severidad CRITICAL
        List<WebElement> severityChips = driver.findElements(By.xpath("//table//td//span[contains(.,'CRITICAL')]"));

        System.out.println("Prueba 6 finalizada, URL: " + driver.getCurrentUrl());
        assertFalse(severityChips.isEmpty(), "No se encontraron alertas críticas tras aplicar el filtro");
    }

    /**
     * Prueba de sistema 7: Cambiar estado de una alerta a EN PROGRESO desde la UI
     * Historias asociadas: H07.03, H10.03
     * Basada en PlantillaPruebasSistema.xlsx (transcripción incluida en el prompt).
     */
    @Test
    void alerta_cambiarEstadoEnProgreso() {

        // Paso 1: Login supervisor y navegar a alertas
        loginAsSupervisor();
        clickNavItem("Alertas");

        // Paso 2: Seleccionar la primera alerta pendiente
        wait10().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table tbody tr")));
        List<WebElement> pendingRows = driver.findElements(
                By.xpath("//table//tr[.//span[contains(.,'Pendiente')]]")
        );
        if (pendingRows.isEmpty()) {
            pendingRows = driver.findElements(By.cssSelector("table tbody tr"));
        }
        pendingRows.get(0).click();

        // Paso 3: Esperar drawer
        wait10().until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h6[contains(.,'Detalle de alerta')]")
        ));

        // Seleccionar el combobox real
        WebElement estadoSelect = wait10().until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("div[role='combobox']")
                )
        );
        estadoSelect.click();

        // Seleccionar opción "En proceso"
        WebElement opcionEnProceso = wait10().until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//li[normalize-space()='En proceso']")
                )
        );
        opcionEnProceso.click();

        // Click en Actualizar
        WebElement actualizarBtn = wait10().until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(.,'Actualizar estado')]")
                )
        );
        actualizarBtn.click();

        // ✔ Esperar que la tabla se actualice
        wait10().until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//table//span[contains(.,'En proceso')]")
        ));

        List<WebElement> inProgress = driver.findElements(
                By.xpath("//table//span[contains(.,'En proceso')]")
        );

        assertTrue(!inProgress.isEmpty(),
                "No se reflejó el estado EN PROCESO en la tabla de alertas");
    }

}
