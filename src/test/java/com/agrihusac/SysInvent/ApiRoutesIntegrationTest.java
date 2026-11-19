package com.agrihusac.SysInvent;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(
        username = "administrador-rutas",
        roles = "ADMIN"
)
class ApiRoutesIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void exponeRutasCanonicasDeLaApi() throws Exception {
        mockMvc.perform(
                        get("/api/v1/catalogos/centros-costo")
                )
                .andExpect(status().isOk());

        mockMvc.perform(
                        get("/api/v1/catalogos/productos")
                )
                .andExpect(status().isOk());

        mockMvc.perform(
                        get("/api/reportes/kardex")
                )
                .andExpect(status().isOk());
    }

    @Test
    void rechazaRutasAntiguasODePlantilla() throws Exception {
        mockMvc.perform(
                        get("/api/v1/productos")
                )
                .andExpect(status().isNotFound());

        mockMvc.perform(
                        get("/api/usuarios/perfil/_plantilla")
                )
                .andExpect(status().isNotFound());
    }
}