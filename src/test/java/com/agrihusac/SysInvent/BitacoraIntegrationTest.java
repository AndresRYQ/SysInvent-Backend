package com.agrihusac.SysInvent;

import com.agrihusac.SysInvent.model.entity.BitacoraEntity;
import com.agrihusac.SysInvent.repository.BitacoraRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(
        username = "auditor",
        authorities = "MODULE_/bitacora"
)
class BitacoraIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BitacoraRepository bitacoraRepository;

    @BeforeEach
    void limpiarBitacora() {
        bitacoraRepository.deleteAll();
    }

    @Test
    void listaBitacoraConFiltrosYDatosDeTrazabilidad() throws Exception {
        BitacoraEntity registro = bitacoraRepository.save(
                BitacoraEntity.builder()
                        .fechaHora(LocalDateTime.now().withNano(0))
                        .modulo("Usuarios")
                        .accion("EDITAR")
                        .detalle("Se actualizaron los datos del usuario")
                        .registroId("USR001")
                        .build()
        );

        String fechaActual = LocalDate.now().toString();

        mockMvc.perform(
                        get("/api/bitacora")
                                .param("busqueda", "actualizaron")
                                .param("modulo", "Usuarios")
                                .param("accion", "EDITAR")
                                .param("fechaDesde", fechaActual)
                                .param("fechaHasta", fechaActual)
                                .param("pagina", "1")
                                .param("tamPagina", "10")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.datos.length()").value(1))
                .andExpect(jsonPath("$.datos[0].bitacoraId")
                        .value(registro.getBitacoraId()))
                .andExpect(jsonPath("$.datos[0].fechaHora").exists())
                .andExpect(jsonPath("$.datos[0].modulo").value("Usuarios"))
                .andExpect(jsonPath("$.datos[0].accion").value("EDITAR"))
                .andExpect(jsonPath("$.datos[0].detalle")
                        .value(containsString("actualizaron")))
                .andExpect(jsonPath("$.datos[0].registroId").value("USR001"))
                .andExpect(jsonPath("$.paginacion.totalElementos").value(1));
    }

    @Test
    void rechazaRangoDeFechasInvalido() throws Exception {
        mockMvc.perform(
                        get("/api/bitacora")
                                .param("fechaDesde", "2026-11-16")
                                .param("fechaHasta", "2026-11-15")
                )
                .andExpect(status().isBadRequest());
    }
}