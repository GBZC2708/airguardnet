package com.airguardnet.system.tests;

import com.airguardnet.system.base.BaseSeleniumTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReportsSystemTest extends BaseSeleniumTest {

    /**
     * Prueba de sistema 13: Ver reporte de uso en pantalla de reportes
     * Historias asociadas: H22.01, H22.02
     * Basada en PlantillaPruebasSistema.xlsx (transcripción incluida en el prompt).
     */
    @Test
    void reportes_verUsoBasico() {
        // Paso 1: Login y abrir módulo Reportes
        loginAsAdmin();
        clickNavItem("Reportes");

        // Paso 2: Validar cabecera y KPIs
        wait10().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h5[contains(.,'Reportes de uso')]")));
        List<WebElement> kpis = driver.findElements(By.xpath("//div[contains(@class,'MuiCard-root')]//p[contains(@class,'MuiTypography-body2')]"));

        System.out.println("Prueba 13 finalizada, URL: " + driver.getCurrentUrl());
        assertTrue(kpis.size() >= 4, "No se cargaron los KPIs de reportes");
    }
}
