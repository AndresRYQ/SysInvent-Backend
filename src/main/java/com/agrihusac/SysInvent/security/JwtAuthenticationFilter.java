package com.agrihusac.SysInvent.security;

import com.agrihusac.SysInvent.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final String BEARER_PREFIX = "Bearer ";
    private static final int ADMIN_ROLE_ID = 1;
    public static final String JWT_ERROR_ATTRIBUTE = "jwtError";

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if (authorization != null && !authorization.isBlank()) {
            if (authorization.regionMatches(true, 0, BEARER_PREFIX, 0, BEARER_PREFIX.length())) {
                String token = authorization.substring(BEARER_PREFIX.length()).trim();

                if (token.isBlank()) {
                    request.setAttribute(JWT_ERROR_ATTRIBUTE, "El token Bearer está vacío");
                } else {
                    autenticar(token, request);
                }
            } else {
                request.setAttribute(
                        JWT_ERROR_ATTRIBUTE,
                        "El encabezado Authorization debe usar el formato Bearer");
            }
        }

        filterChain.doFilter(request, response);
    }

    private void autenticar(String token, HttpServletRequest request) {
        try {
            Claims claims = jwtService.parseToken(token);
            String usuario = claims.getSubject();

            if (usuario == null || usuario.isBlank()) {
                request.setAttribute(JWT_ERROR_ATTRIBUTE, "El token no contiene un usuario válido");
                return;
            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            usuario, null, obtenerAuthorities(claims));
            authentication.setDetails(claims);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (ExpiredJwtException exception) {
            LOGGER.warn("JWT diagnóstico: token expirado");
            request.setAttribute(JWT_ERROR_ATTRIBUTE, "El token ha expirado");
        } catch (RuntimeException exception) {
            LOGGER.warn(
                    "JWT diagnóstico: token inválido, causa={} - {}",
                    exception.getClass().getSimpleName(),
                    exception.getMessage());
            request.setAttribute(JWT_ERROR_ATTRIBUTE, "El token no es válido");
        }
    }

    private List<SimpleGrantedAuthority> obtenerAuthorities(Claims claims) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Object rolesClaim = claims.get("roles");
        if (!(rolesClaim instanceof Collection<?> roles)) {
            return authorities;
        }

        for (Object roleObject : roles) {
            if (!(roleObject instanceof Map<?, ?> role)) {
                continue;
            }

            if (esAdministrador(role.get("rolId"))) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }

            Object nombre = role.get("nombre");
            if (nombre instanceof String nombreRol && !nombreRol.isBlank()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + nombreRol));
            }
        }

        return authorities;
    }

    private boolean esAdministrador(Object rolId) {
        if (rolId instanceof Number number) {
            return number.intValue() == ADMIN_ROLE_ID;
        }
        if (rolId instanceof String value) {
            try {
                return Integer.parseInt(value) == ADMIN_ROLE_ID;
            } catch (NumberFormatException ignored) {
                return false;
            }
        }
        return false;
    }
}
