package com.agrihusac.SysInvent;

import com.agrihusac.SysInvent.model.entity.UsuarioEntity;
import com.agrihusac.SysInvent.service.JwtService;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RolesSecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Test
    void listaRolesConJwtYPaginacionDesdeUno() throws Exception {
        UsuarioEntity usuario = UsuarioEntity.builder()
                .usuarioId(1)
                .usuario("admin-prueba")
                .email("admin-prueba@example.test")
                .activo(Boolean.TRUE)
                .build();

        String token = jwtService.generateToken(
                usuario,
                1,
                List.of());

        mockMvc.perform(get("/api/roles")
                        .param("pagina", "1")
                        .param("tamPagina", "100")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.datos").isArray())
                .andExpect(jsonPath("$.paginacion.numeroPagina").value(1))
                .andExpect(jsonPath("$.paginacion.tamanioPagina").value(100));
    }

    @Test
    void rechazaRolesSinJwt() throws Exception {
        mockMvc.perform(get("/api/roles")
                        .param("pagina", "1")
                        .param("tamPagina", "10"))
                .andExpect(status().isUnauthorized());
    }
}
