package com.airguardnet.system.tests;

import com.airguardnet.system.base.BaseSeleniumTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DashboardSystemTest extends BaseSeleniumTest {

    /**
     * Prueba de sistema 5: Ver KPIs principales en dashboard
     * Historias asociadas: H11.01, H22.01
     * Basada en PlantillaPruebasSistema.xlsx (transcripción incluida en el prompt).
     */
    @Test
    void dashboard_muestraTarjetasKpi() {
        // Paso 1: Login y acceso al dashboard
        loginAsAdmin();
        wait10().until(ExpectedConditions.urlContains("/dashboard"));

        // Paso 2: Ubicar tarjetas de KPI
        List<WebElement> kpiCards = driver.findElements(By.xpath("//div[contains(@class,'MuiCard-root')]//p[contains(@class,'MuiTypography-body2') and (text()='Usuarios' or text()='Dispositivos' or text()='Lecturas' or text()='Alertas críticas')]"));

        // Paso 3: Validar que existen al menos cuatro tarjetas
        boolean enoughCards = kpiCards.size() >= 4;

        System.out.println("Prueba 5 finalizada, URL: " + driver.getCurrentUrl());
        assertTrue(enoughCards, "No se encontraron las tarjetas KPI esperadas en el dashboard");
    }
}
