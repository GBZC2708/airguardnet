package com.airguardnet.system.tests;

import com.airguardnet.system.base.BaseSeleniumTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SimulationSystemTest extends BaseSeleniumTest {

    /**
     * Prueba de sistema 15: Simulación de datos en módulo Simulación
     * Historias asociadas: H08.03, H14.01
     * Basada en PlantillaPruebasSistema.xlsx (transcripción incluida en el prompt).
     */
    @Test
    void simulacion_picoDePolvo() {
        // Paso 1: Login y abrir Simulación
        loginAsAdmin();
        clickNavItem("Simulación");
        wait10().until(ExpectedConditions.urlContains("/simulation"));

        // Paso 2: Capturar el texto actual de "Último PM2.5"
        By ultimoPmLocator = By.xpath("//p[contains(.,'Último PM2.5')]");
        String initialText = wait10()
                .until(ExpectedConditions.visibilityOfElementLocated(ultimoPmLocator))
                .getText();

        // Paso 3: Lanzar pico de polvo
        driver.findElement(By.xpath("//button[contains(.,'Pico de polvo')]")).click();

        // Paso 4: Esperar a que el texto cambie (se actualiza la simulación)
        FluentWait<?> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(15))
                .pollingEvery(Duration.ofSeconds(2));

        Boolean changed = wait.until(d -> {
            String updatedText = driver.findElement(ultimoPmLocator).getText();
            System.out.println("Simulación - antes: " + initialText + " | ahora: " + updatedText);
            return !updatedText.equals(initialText);
        });

        System.out.println("Prueba 15 finalizada, URL: " + driver.getCurrentUrl());
        assertTrue(Boolean.TRUE.equals(changed),
                "El valor mostrado de PM2.5 no cambió tras activar el pico de polvo");
    }

}
