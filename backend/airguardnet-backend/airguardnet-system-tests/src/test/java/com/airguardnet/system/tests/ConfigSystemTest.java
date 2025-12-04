package com.airguardnet.system.tests;

import com.airguardnet.system.base.BaseSeleniumTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConfigSystemTest extends BaseSeleniumTest {

    /**
     * Prueba de sistema 8: Ver parámetros globales en pantalla de configuración
     * Historias asociadas: H23.02, H09.02
     * Basada en PlantillaPruebasSistema.xlsx (transcripción incluida en el prompt).
     */
    @Test
    void configuracion_verParametrosGlobales() {
        // Paso 1: Login como admin y navegar a Configuración
        loginAsAdmin();
        clickNavItem("Configuración");
        wait10().until(ExpectedConditions.urlContains("/config/system"));

        // Paso 2: Validar texto guía y campo de minutos offline
        boolean headingVisible = !driver.findElements(By.xpath("//h5[contains(.,'Configuración del sistema')]")).isEmpty();
        boolean inputVisible = !driver.findElements(By.xpath("//label[contains(.,'OFFLINE')]/following::input[1]")).isEmpty();

        System.out.println("Prueba 8 finalizada, URL: " + driver.getCurrentUrl());
        assertTrue(headingVisible && inputVisible, "No se mostraron los parámetros globales esperados");
    }

    /**
     * Prueba de sistema 9: Editar parámetro DEVICE_OFFLINE_MINUTES desde UI
     * Historias asociadas: H23.02, H23.03
     * Basada en PlantillaPruebasSistema.xlsx (transcripción incluida en el prompt).
     */
    @Test
    void configuracion_editarDeviceOfflineMinutes() {
        // Paso 1: Login y abrir configuración
        loginAsAdmin();
        clickNavItem("Configuración");

        // Paso 2: Editar valor a 20
        WebElement input = wait10().until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//label[contains(.,'OFFLINE')]/following::input[1]")));
        input.clear();
        input.sendKeys("20");

        // Paso 3: Guardar cambios
        wait10().until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(.,'Guardar cambios')]"))).click();

        // Paso 4: Confirmar mensaje de éxito
        wait10().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'MuiAlert-root') and contains(.,'actualizado')]")));
        String value = driver.findElement(By.xpath("//label[contains(.,'OFFLINE')]/following::input[1]")).getAttribute("value");

        System.out.println("Prueba 9 finalizada, URL: " + driver.getCurrentUrl());
        assertEquals("20", value, "El valor de DEVICE_OFFLINE_MINUTES no se actualizó en pantalla");
    }

    /**
     * Prueba de sistema 10: Ver historial de cambios de configuración
     * Historias asociadas: H23.03
     * Basada en PlantillaPruebasSistema.xlsx (transcripción incluida en el prompt).
     */
    @Test
    void configuracion_verHistorialCambios() {
        // Paso 1: Login y navegar a pestaña historial de cambios
        loginAsAdmin();
        clickNavItem("Configuración");
        wait10().until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(.,'Historial de cambios')] | //div[@role='tab' and .='Historial de cambios'] | //span[text()='Historial de cambios']")));
        driver.findElement(By.xpath("//span[text()='Historial de cambios'] | //button[contains(.,'Historial de cambios')] | //div[@role='tab' and .='Historial de cambios']"))
                .click();

        // Paso 2: Validar que hay filas en la tabla
        wait10().until(ExpectedConditions.urlContains("/config/logs"));
        List<WebElement> rows = findTableRows();

        System.out.println("Prueba 10 finalizada, URL: " + driver.getCurrentUrl());
        assertFalse(rows.isEmpty(), "No se encontraron registros en el historial de cambios");
    }
}
