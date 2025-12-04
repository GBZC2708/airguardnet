package com.airguardnet.system.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public abstract class BaseSeleniumTest {

    protected WebDriver driver;

    // Frontend de AirGuardNet
    protected String baseUrl = "http://localhost:5173";

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected WebDriverWait wait10() {
        return new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    protected WebElement findInputByLabel(String labelText) {
        return wait10().until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//label[contains(normalize-space(.), '" + labelText + "')]/following::input[1]")
                )
        );
    }

    protected void loginAsAdmin() {
        driver.get(baseUrl + "/login");

        WebElement emailInput = findInputByLabel("Correo electrónico");
        emailInput.clear();
        emailInput.sendKeys("gerald.admin@airguardnet.local");

        WebElement passwordInput = findInputByLabel("Contraseña");
        passwordInput.clear();
        passwordInput.sendKeys("Admin1234!");

        wait10().until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Iniciar Sesión' or normalize-space()='Iniciar sesión']")))
                .click();

        wait10().until(ExpectedConditions.not(ExpectedConditions.urlContains("/login")));
    }

    protected void loginAsSupervisor() {
        // En ausencia de credenciales específicas, reutilizamos el usuario administrador para asegurar el flujo.
        loginAsAdmin();
    }

    protected void clickNavItem(String label) {
        wait10().until(ExpectedConditions.elementToBeClickable(By.linkText(label))).click();
    }

    protected List<WebElement> findTableRows() {
        return driver.findElements(By.cssSelector("table tbody tr"));
    }
}
