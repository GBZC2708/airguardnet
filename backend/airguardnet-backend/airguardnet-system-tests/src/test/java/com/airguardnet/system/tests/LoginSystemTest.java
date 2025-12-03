package com.airguardnet.system.tests;

import com.airguardnet.system.base.BaseSeleniumTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginSystemTest extends BaseSeleniumTest {

    @Test
    void login_conCredencialesValidas_debeIrAlDashboard() {

        // 1. Ir a la pantalla de login
        driver.get(baseUrl + "/login");

        // 2. Completar correo y contraseña (IDs reales de los inputs MUI)
        driver.findElement(By.id("_r_1_"))
                .sendKeys("gerald.admin@airguardnet.local");

        driver.findElement(By.id("_r_2_"))
                .sendKeys("Admin1234!");

        // 3. Esperar y hacer clic en el botón de tipo submit
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("button[type='submit']"))
        ).click();

        // 4. Esperar a que la URL CAMBIE (que ya no sea /login)
        wait.until(
                ExpectedConditions.not(
                        ExpectedConditions.urlContains("/login")
                )
        );

        String currentUrl = driver.getCurrentUrl();
        System.out.println("URL final después del login = " + currentUrl);

        // 5. Verificar: ya no estamos en la pantalla de /login
        boolean loggedIn = !currentUrl.contains("/login");

        assertTrue(loggedIn,
                "No redirigió fuera de /login. URL final: " + currentUrl);
    }
}
