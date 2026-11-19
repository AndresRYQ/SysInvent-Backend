package com.agrihusac.SysInvent;

import com.agrihusac.SysInvent.api.ValeConsumoController;
import com.agrihusac.SysInvent.config.SecurityConfig;
import com.agrihusac.SysInvent.model.request.valeconsumo.ValeConsumoRequest;
import com.agrihusac.SysInvent.security.JwtAccessDeniedHandler;
import com.agrihusac.SysInvent.security.JwtAuthenticationEntryPoint;
import com.agrihusac.SysInvent.security.JwtAuthenticationFilter;
import com.agrihusac.SysInvent.service.JwtService;
import com.agrihusac.SysInvent.service.valeconsumo.ValeConsumoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ValeConsumoController.class)
@Import({
        SecurityConfig.class,
        JwtAuthenticationFilter.class,
        JwtAuthenticationEntryPoint.class,
        JwtAccessDeniedHandler.class
})
class ValeConsumoSecurityTest {

    private static final String VALE_JSON = """
            {
              "fechaVale": "2026-11-15",
              "centroCostoId": 1,
              "solicitante": "Solicitante de prueba",
              "motivo": "Prueba de seguridad",
              "detalles": [
                {
                  "productoId": 1,
                  "distribuciones": [
                    {
                      "destinoId": 1,
                      "parteEquipoId": null,
                      "cantidad": 1
                    }
                  ]
                }
              ]
            }
            """;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private ValeConsumoService valeConsumoService;

    @BeforeEach
    void configurarServicio() {
        when(valeConsumoService.obtener(1))
                .thenReturn(null);

        when(valeConsumoService.crear(
                any(ValeConsumoRequest.class)
        )).thenReturn(null);
    }

    @Test
    void rechazaConsultaSinAutenticacion() throws Exception {
        mockMvc.perform(
                        get("/api/vales-consumo/1")
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void rechazaUsuarioSinModuloDeVales() throws Exception {
        mockMvc.perform(
                        get("/api/vales-consumo/1")
                                .with(user("usuario-prueba")
                                        .authorities(
                                                new SimpleGrantedAuthority(
                                                        "MODULE_/usuarios"
                                                )
                                        ))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void permiteConsultarConModuloDeVales() throws Exception {
        mockMvc.perform(
                        get("/api/vales-consumo/1")
                                .with(user("almacenero")
                                        .authorities(
                                                new SimpleGrantedAuthority(
                                                        "MODULE_/vales-consumo"
                                                )
                                        ))
                )
                .andExpect(status().isOk());
    }

    @Test
    void permiteRegistrarConModuloDeVales() throws Exception {
        mockMvc.perform(
                        post("/api/vales-consumo")
                                .with(user("almacenero")
                                        .authorities(
                                                new SimpleGrantedAuthority(
                                                        "MODULE_/vales-consumo"
                                                )
                                        ))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(VALE_JSON)
                )
                .andExpect(status().isCreated());
    }

    @Test
    void permiteAccesoAlAdministrador() throws Exception {
        mockMvc.perform(
                        post("/api/vales-consumo")
                                .with(user("administrador")
                                        .authorities(
                                                new SimpleGrantedAuthority(
                                                        "ROLE_ADMIN"
                                                )
                                        ))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(VALE_JSON)
                )
                .andExpect(status().isCreated());
    }
}