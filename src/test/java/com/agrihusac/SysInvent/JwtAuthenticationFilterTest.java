package com.agrihusac.SysInvent;

import com.agrihusac.SysInvent.security.JwtAuthenticationFilter;
import com.agrihusac.SysInvent.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    private JwtAuthenticationFilter filter;

    @BeforeEach
    void configurar() {
        filter = new JwtAuthenticationFilter(jwtService);
    }

    @AfterEach
    void limpiarContexto() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void convierteModulosDelJwtEnAuthorities() throws Exception {
        Claims claims = Jwts.claims();
        claims.setSubject("usuario-almacen");
        claims.put("rolId", 2);
        claims.put(
                "modulos",
                List.of(
                        Map.of("ruta", "/usuarios"),
                        Map.of("ruta", "roles")
                )
        );

        when(jwtService.parseToken("token-valido"))
                .thenReturn(claims);

        MockHttpServletRequest request =
                new MockHttpServletRequest(
                        "GET",
                        "/api/usuarios"
                );

        request.addHeader(
                "Authorization",
                "Bearer token-valido"
        );

        filter.doFilter(
                request,
                new MockHttpServletResponse(),
                (servletRequest, servletResponse) -> {
                }
        );

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        Set<String> authorities =
                authentication
                        .getAuthorities()
                        .stream()
                        .map(authority ->
                                authority.getAuthority())
                        .collect(Collectors.toSet());

        assertEquals(
                Set.of(
                        "MODULE_/usuarios",
                        "MODULE_/roles"
                ),
                authorities
        );
    }

    @Test
    void asignaAuthorityDeAdministradorAlRolUno()
            throws Exception {
        Claims claims = Jwts.claims();
        claims.setSubject("administrador");
        claims.put("rolId", 1);
        claims.put("modulos", List.of());

        when(jwtService.parseToken("token-admin"))
                .thenReturn(claims);

        MockHttpServletRequest request =
                new MockHttpServletRequest(
                        "GET",
                        "/api/roles"
                );

        request.addHeader(
                "Authorization",
                "Bearer token-admin"
        );

        filter.doFilter(
                request,
                new MockHttpServletResponse(),
                (servletRequest, servletResponse) -> {
                }
        );

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        Set<String> authorities =
                authentication
                        .getAuthorities()
                        .stream()
                        .map(authority ->
                                authority.getAuthority())
                        .collect(Collectors.toSet());

        assertEquals(
                Set.of("ROLE_ADMIN"),
                authorities
        );
    }
}