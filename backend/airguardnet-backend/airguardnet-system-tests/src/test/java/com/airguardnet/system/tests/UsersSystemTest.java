package com.airguardnet.system.tests;

import com.airguardnet.system.base.BaseSeleniumTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UsersSystemTest extends BaseSeleniumTest {

    /**
     * Prueba de sistema 14: Ver listado de usuarios en módulo Usuarios
     * Historias asociadas: H02.03, H11.02
     * Basada en PlantillaPruebasSistema.xlsx (transcripción incluida en el prompt).
     */
    @Test
    void usuarios_verListaUsuarios() {
        // Paso 1: Login admin y abrir Usuarios
        loginAsAdmin();
        clickNavItem("Usuarios");

        // Paso 2: Validar que hay varias filas en la tabla
        wait10().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table tbody tr")));
        List<WebElement> rows = findTableRows();

        System.out.println("Prueba 14 finalizada, URL: " + driver.getCurrentUrl());
        assertTrue(rows.size() >= 4, "Se esperaban al menos 4 usuarios en la tabla");
    }

    /**
     * Prueba de sistema 17: Validar resaltado de campos requeridos en formulario de creación de usuario
     * Historias asociadas: H25.01, H25.03
     * Basada en PlantillaPruebasSistema.xlsx (transcripción incluida en el prompt).
     */
    @Test
    void crearUsuario_sinEmail_muestraError() {
        // Paso 1: Login admin y abrir Usuarios
        loginAsAdmin();
        clickNavItem("Usuarios");

        // Paso 2: Abrir modal de nuevo usuario
        wait10().until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Nuevo usuario']"))).click();
        wait10().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(.,'Nuevo usuario')]")));

        // Paso 3: Completar campos excepto correo
        driver.findElement(By.xpath("//label[contains(.,'Nombre')]/following::input[1]")).sendKeys("QA");
        driver.findElement(By.xpath("//label[contains(.,'Apellido')]/following::input[1]")).sendKeys("Automation");
        driver.findElement(By.xpath("//label[contains(.,'Contraseña')]/following::input[1]")).sendKeys("Password123!");

        // Paso 4: Verificar que botón Guardar permanece deshabilitado si falta el email
        WebElement saveButton = driver.findElement(By.xpath("//button[normalize-space()='Guardar']"));
        boolean disabled = !saveButton.isEnabled();

        System.out.println("Prueba 17 finalizada, URL: " + driver.getCurrentUrl());
        assertTrue(disabled, "El botón Guardar no debería habilitarse cuando falta el correo electrónico");
    }
}
