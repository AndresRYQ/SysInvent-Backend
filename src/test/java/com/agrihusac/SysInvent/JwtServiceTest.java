package com.agrihusac.SysInvent;

import com.agrihusac.SysInvent.model.entity.UsuarioEntity;
import com.agrihusac.SysInvent.service.JwtService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTest {

    private static final String JWT_SECRET =
            "0123456789ABCDEF0123456789ABCDEF";

    @Test
    void generaYValidaTokenConClaimsDeUsuarioRolYModulos() {
        JwtService jwtService = new JwtService(JWT_SECRET, 60_000);

        UsuarioEntity usuario = UsuarioEntity.builder()
                .usuarioId(7)
                .usuario("usuario-prueba")
                .nombres("Juan")
                .apePaterno("Perez")
                .email("juan.perez@empresa.com")
                .activo(true)
                .build();

        List<Map<String, Object>> modulos = List.of(
                Map.of(
                        "moduloId", 3,
                        "nombre", "Productos",
                        "ruta", "/productos"
                )
        );

        String token = jwtService.generateToken(
                usuario,
                2,
                "ALMACEN",
                modulos
        );

        Claims claims = jwtService.parseToken(token);

        assertNotNull(token);
        assertEquals("usuario-prueba", claims.getSubject());
        assertEquals(7, claims.get("usuarioId", Integer.class));
        assertEquals("Juan Perez", claims.get("nombre", String.class));
        assertEquals("juan.perez@empresa.com", claims.get("email", String.class));
        assertEquals(2, claims.get("rolId", Integer.class));
        assertEquals("ALMACEN", claims.get("nombreRol", String.class));
        assertNotNull(claims.get("modulos"));
        assertEquals(1, ((List<?>) claims.get("modulos")).size());
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
        assertTrue(claims.getExpiration().after(claims.getIssuedAt()));
    }
}