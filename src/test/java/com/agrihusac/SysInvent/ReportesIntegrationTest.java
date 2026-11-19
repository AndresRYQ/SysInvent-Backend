package com.agrihusac.SysInvent;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "administrador-reportes", roles = "ADMIN")
class ReportesIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listaReporteDeIngresos() throws Exception {
        mockMvc.perform(get("/api/reportes/ingresos")
                        .param("pagina", "1")
                        .param("tamPagina", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.datos").isArray());
    }

    @Test
    void listaReporteDeVales() throws Exception {
        mockMvc.perform(get("/api/reportes/vales")
                        .param("pagina", "1")
                        .param("tamPagina", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.datos").isArray());
    }

    @Test
    void listaProductosMasPedidos() throws Exception {
        mockMvc.perform(get("/api/reportes/productos-mas-pedidos")
                        .param("pagina", "1")
                        .param("tamPagina", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.datos").isArray());
    }

    @Test
    void generaReporteKardex() throws Exception {
        mockMvc.perform(get("/api/reportes/kardex"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }
}