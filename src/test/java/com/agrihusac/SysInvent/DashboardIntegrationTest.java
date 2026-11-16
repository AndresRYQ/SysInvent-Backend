package com.agrihusac.SysInvent;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "usuario-dashboard")
class DashboardIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void obtieneIndicadoresYActividadDesdeLaBaseDeDatos() throws Exception {
        mockMvc.perform(get("/api/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cards").exists())
                .andExpect(jsonPath("$.cards.productosActivos")
                        .value(greaterThanOrEqualTo(0)))
                .andExpect(jsonPath("$.cards.stockCritico")
                        .value(greaterThanOrEqualTo(0)))
                .andExpect(jsonPath("$.cards.productosSinStock")
                        .value(greaterThanOrEqualTo(0)))
                .andExpect(jsonPath("$.cards.ingresosMes")
                        .value(greaterThanOrEqualTo(0)))
                .andExpect(jsonPath("$.cards.valesMes")
                        .value(greaterThanOrEqualTo(0)))
                .andExpect(jsonPath("$.cards.movimientosHoy")
                        .value(greaterThanOrEqualTo(0)))
                .andExpect(jsonPath("$.seguimiento").isArray())
                .andExpect(jsonPath("$.actividadReciente").isArray());
    }
}