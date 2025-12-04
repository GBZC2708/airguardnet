package com.airguardnet.system.tests;

import com.airguardnet.system.base.BaseSeleniumTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DocsSystemTest extends BaseSeleniumTest {

    /**
     * Prueba de sistema 18: Visualizar historial de versiones de la aplicación
     * Historias asociadas: H21.01, H21.03
     * Basada en PlantillaPruebasSistema.xlsx (transcripción incluida en el prompt).
     */
    @Test
    void documentacion_verHistorialVersiones() {
        // Paso 1: Login admin
        loginAsAdmin();

        // Paso 2: Navegar a historial de versiones
        driver.get(baseUrl + "/docs/versions");
        wait10().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h5[contains(.,'Historial de versiones')]")));

        // Paso 3: Verificar que se listan versiones
        List<WebElement> versions = driver.findElements(By.xpath("//table//tr[td[contains(.,'v')]]"));

        System.out.println("Prueba 18 finalizada, URL: " + driver.getCurrentUrl());
        assertTrue(versions.size() >= 2, "No se listaron las versiones esperadas en la documentación");
    }
}
