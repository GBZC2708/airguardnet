// Cobertura matriz Nro 14–15
package com.airguardnet.user.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private static final String SECRET = "supersecretkeysupersecretkey12";

    // Nro 14: Generar JWT incluye role y plan
    @Test
    void generateToken_incluyeRoleYPlan() {
        JwtService jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", SECRET);
        ReflectionTestUtils.setField(jwtService, "expirationMs", 3_600_000L);

        String token = jwtService.generateToken(1L, "ADMIN", 99L);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals("ADMIN", claims.get("role"));
        assertEquals(99L, ((Number) claims.get("planId")).longValue());
        assertEquals("1", claims.getSubject());
    }

    // Nro 15: Validar expiración de token
    @Test
    void isExpired_tokenVencido_true() {
        JwtService jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", SECRET);
        ReflectionTestUtils.setField(jwtService, "expirationMs", -1_000L);

        String token = jwtService.generateToken(1L, "ADMIN", 99L);

        assertThrows(ExpiredJwtException.class, () ->
                Jwts.parserBuilder()
                        .setSigningKey(SECRET.getBytes())
                        .build()
                        .parseClaimsJws(token));
    }
}
