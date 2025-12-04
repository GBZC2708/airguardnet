package com.airguardnet.system.tests;

import com.airguardnet.system.base.BaseSeleniumTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DevicesSystemTest extends BaseSeleniumTest {

    /**
     * Prueba de sistema 3: Navegar al módulo Dispositivos desde el sidebar
     * Historias asociadas: H05.01, H11.02
     * Basada en PlantillaPruebasSistema.xlsx (transcripción incluida en el prompt).
     */
    @Test
    void navegarADispositivos_muestraLista() {
        // Paso 1: Login como admin
        loginAsAdmin();

        // Paso 2: Ir a Dispositivos desde el menú lateral
        clickNavItem("Dispositivos");

        // Paso 3: Esperar cabecera y tarjetas
        wait10().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h5[contains(.,'Dispositivos')]")));
        List<WebElement> cards = driver.findElements(By.xpath("//a[contains(@href,'/devices/') and .//p[contains(text(),'UID:')]]"));

        System.out.println("Prueba 3 finalizada, URL: " + driver.getCurrentUrl());
        assertFalse(cards.isEmpty(), "No se encontraron dispositivos en la lista");
    }

    /**
     * Prueba de sistema 4: Ver detalle de un dispositivo desde la lista
     * Historias asociadas: H05.02, H05.03
     * Basada en PlantillaPruebasSistema.xlsx (transcripción incluida en el prompt).
     */
    @Test
    void verDetalleDispositivo_desdeLista() {
        // Paso 1: Login y navegar a dispositivos
        loginAsAdmin();
        clickNavItem("Dispositivos");

        // Paso 2: Abrir el primer dispositivo
        wait10().until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@href,'/devices/')]") )).click();

        // Paso 3: Validar que se muestra nombre y UID en detalle
        wait10().until(ExpectedConditions.urlContains("/devices/"));
        boolean hasUid = !driver.findElements(By.xpath("//p[contains(text(),'UID:')]")).isEmpty();
        boolean hasPm25 = !driver.findElements(By.xpath("//th[contains(.,'PM2.5')]")).isEmpty();

        System.out.println("Prueba 4 finalizada, URL: " + driver.getCurrentUrl());
        assertTrue(hasUid && hasPm25, "No se mostraron los datos esperados del dispositivo");
    }
}
