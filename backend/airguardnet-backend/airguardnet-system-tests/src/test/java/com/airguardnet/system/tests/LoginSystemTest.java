package com.airguardnet.system.tests;

import com.airguardnet.system.base.BaseSeleniumTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginSystemTest extends BaseSeleniumTest {

    /**
     * Prueba de sistema 1: Login exitoso como ADMIN desde pantalla principal
     * Historias asociadas: H01.02, H11.01
     * Basada en PlantillaPruebasSistema.xlsx (transcripción incluida en el prompt).
     */
    @Test
    void login_conCredencialesValidas_debeIrAlDashboard() {

        // Paso 1: Ir a la pantalla de login
        driver.get(baseUrl + "/login");

        // Paso 2: Completar correo y contraseña usando etiquetas visibles
        findInputByLabel("Correo electrónico").sendKeys("gerald.admin@airguardnet.local");
        findInputByLabel("Contraseña").sendKeys("Admin1234!");

        // Paso 3: Esperar y hacer clic en el botón de tipo submit
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[normalize-space()='Iniciar Sesión' or normalize-space()='Iniciar sesión']"))
        ).click();

        // Paso 4: Esperar a que la URL CAMBIE (que ya no sea /login)
        wait.until(
                ExpectedConditions.not(
                        ExpectedConditions.urlContains("/login")
                )
        );

        String currentUrl = driver.getCurrentUrl();
        System.out.println("URL final después del login = " + currentUrl);

        // Paso 5: Verificar: ya no estamos en la pantalla de /login
        boolean loggedIn = !currentUrl.contains("/login");

        assertTrue(loggedIn,
                "No redirigió fuera de /login. URL final: " + currentUrl);
    }

    /**
     * Prueba de sistema 2: Login fallido con password incorrecto muestra mensaje de error
     * Historias asociadas: H01.02, H25.02
     * Basada en PlantillaPruebasSistema.xlsx (transcripción incluida en el prompt).
     */
    @Test
    void login_conPasswordInvalido_muestraMensajeError() {
        // Paso 1: Ir a login
        driver.get(baseUrl + "/login");

        // Paso 2: Completar email válido y password inválido
        findInputByLabel("Correo electrónico").sendKeys("gerald.admin@airguardnet.local");
        findInputByLabel("Contraseña").sendKeys("ClaveMala");

        // Paso 3: Intentar enviar el formulario
        wait10().until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[normalize-space()='Iniciar Sesión' or normalize-space()='Iniciar sesión']")))
                .click();

        // Paso 4: Validar que seguimos en /login y aparece alerta de error
        wait10().until(ExpectedConditions.urlContains("/login"));
        boolean errorVisible = !driver.findElements(By.xpath("//div[contains(@class,'MuiAlert-root') and contains(.,'Correo o contraseña incorrectos')]"))
                .isEmpty();

        System.out.println("Prueba 2 finalizada, URL: " + driver.getCurrentUrl());
        assertTrue(errorVisible, "No se mostró el mensaje de error esperado en login fallido");
    }

    /**
     * Prueba de sistema 16: Cerrar sesión desde el frontend
     * Historias asociadas: H12.03
     * Basada en PlantillaPruebasSistema.xlsx (transcripción incluida en el prompt).
     */
    @Test
    void logout_redirigeALogin() {
        // Paso 1: Login exitoso
        loginAsAdmin();

        // Paso 2: Hacer clic en el botón Cerrar sesión del sidebar
        wait10().until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Cerrar sesión']"))).click();

        // Paso 3: Validar redirección a /login
        wait10().until(ExpectedConditions.urlContains("/login"));

        System.out.println("Prueba 16 finalizada, URL: " + driver.getCurrentUrl());
        assertTrue(driver.getCurrentUrl().contains("/login"), "No se redirigió a la pantalla de login tras el logout");
    }
}
