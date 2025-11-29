package com.airguardnet.user.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // TODO: aquí luego leeremos Authorization: Bearer <token>
        // y usaremos JwtService para validar y setear el usuario en el contexto.
        // Por ahora lo dejamos pasar tal cual.

        filterChain.doFilter(request, response);
    }
}
