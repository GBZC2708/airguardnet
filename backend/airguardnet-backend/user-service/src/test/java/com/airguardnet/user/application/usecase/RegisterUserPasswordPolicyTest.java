package com.airguardnet.user.application.usecase;

import com.airguardnet.user.domain.repository.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserPasswordPolicyTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    private RegisterUserUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new RegisterUserUseCase(userRepositoryPort, passwordEncoder);
    }

    private boolean invokeIsPasswordValid(String password) {
        try {
            Method method = RegisterUserUseCase.class.getDeclaredMethod("isPasswordValid", String.class);
            method.setAccessible(true);
            return (boolean) method.invoke(useCase, password);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    // Nro 1: Validar longitud mínima de contraseña
    @Test
    void hasMinimumLength_passwordLarga_true() {
        assertTrue(invokeIsPasswordValid("Admin1234!"));
    }

    // Nro 2: Rechazar contraseña corta
    @Test
    void hasMinimumLength_passwordCorta_false() {
        assertFalse(invokeIsPasswordValid("A1b4"));
    }

    // Nro 3: Validar presencia de mayúscula
    @Test
    void hasUppercase_contieneMayuscula_true() {
        assertTrue(invokeIsPasswordValid("Admin1234!"));
    }

    // Nro 4: Rechazar contraseña sin mayúscula
    @Test
    void hasUppercase_sinMayuscula_false() {
        assertFalse(invokeIsPasswordValid("admin1234"));
    }

    // Nro 5: Validar presencia de minúscula
    @Test
    void hasLowercase_contieneMinuscula_true() {
        assertTrue(invokeIsPasswordValid("Admin1234!"));
    }

    // Nro 6: Validar presencia de dígito
    @Test
    void hasDigit_contieneDigito_true() {
        assertTrue(invokeIsPasswordValid("Admin1234!"));
    }

    // Nro 7: Password válida cumple todas las reglas
    @Test
    void isValidPassword_passwordFuerte_true() {
        assertTrue(invokeIsPasswordValid("Admin1234!"));
    }

    // Nro 8: Password inválida por faltar dígito
    @Test
    void isValidPassword_sinDigito_false() {
        assertFalse(invokeIsPasswordValid("AdminClave"));
    }
}
