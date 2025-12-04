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

        // Paso 2: Capturar el PM2.5 actual
        String initialText = wait10().until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(.,'Último PM2.5')]"))).getText();
        double initialValue = parsePmValue(initialText);

        // Paso 3: Lanzar pico de polvo
        driver.findElement(By.xpath("//button[contains(.,'Pico de polvo')]")).click();

        // Paso 4: Esperar a que el valor aumente
        FluentWait<?> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(15))
                .pollingEvery(Duration.ofSeconds(2));

        Boolean increased = wait.until(d -> {
            String updatedText = driver.findElement(By.xpath("//p[contains(.,'Último PM2.5')]")).getText();
            double updatedValue = parsePmValue(updatedText);
            return updatedValue > initialValue;
        });

        System.out.println("Prueba 15 finalizada, URL: " + driver.getCurrentUrl());
        assertTrue(Boolean.TRUE.equals(increased), "El PM2.5 simulado no aumentó tras el pico de polvo");
    }

    private double parsePmValue(String text) {
        try {
            String number = text.replaceAll("[^0-9.,]", "").replace(",", ".");
            return Double.parseDouble(number);
        } catch (Exception e) {
            return 0d;
        }
    }
}
