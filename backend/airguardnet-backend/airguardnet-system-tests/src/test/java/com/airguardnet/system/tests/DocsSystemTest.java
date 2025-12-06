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
     * Prueba de sistema 18: Visualizar documentación de versiones / guías
     * Historias asociadas: H21.01, H21.03
     * Adaptada a la UI actual (pantalla /docs con tarjetas de guías).
     */
    @Test
    void documentacion_verHistorialVersiones() {
        // Paso 1: Login admin
        loginAsAdmin();

        // Paso 2: Navegar a documentación (pantalla actual)
        clickNavItem("Documentación");
        wait10().until(ExpectedConditions.urlContains("/docs"));

        // Paso 3: Esperar a que cargue la cabecera de documentación
        wait10().until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(.,'Documentación')] | //*[contains(.,'Bienvenido a la Documentación')]")
        ));

        // Paso 4: Verificar que se listan varias guías / tarjetas
        List<WebElement> entries = driver.findElements(
                By.xpath("//*[contains(.,'Guía') or contains(.,'Video tutorial')]")
        );

        System.out.println("Prueba 18 finalizada, URL: " + driver.getCurrentUrl()
                + " - entradas de documentación: " + entries.size());

        assertTrue(entries.size() >= 2,
                "No se encontraron suficientes entradas de documentación en la pantalla /docs");
    }

}
