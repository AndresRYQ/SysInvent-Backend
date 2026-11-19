package com.agrihusac.SysInvent;

import com.agrihusac.SysInvent.api.BitacoraController;
import com.agrihusac.SysInvent.config.SecurityConfig;
import com.agrihusac.SysInvent.model.request.BitacoraRequest;
import com.agrihusac.SysInvent.security.JwtAccessDeniedHandler;
import com.agrihusac.SysInvent.security.JwtAuthenticationEntryPoint;
import com.agrihusac.SysInvent.security.JwtAuthenticationFilter;
import com.agrihusac.SysInvent.service.JwtService;
import com.agrihusac.SysInvent.service.bitacora.BitacoraService;
import com.agrihusac.SysInvent.utils.CustomPage;
import com.agrihusac.SysInvent.utils.MessageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BitacoraController.class)
@Import({
        SecurityConfig.class,
        JwtAuthenticationFilter.class,
        JwtAuthenticationEntryPoint.class,
        JwtAccessDeniedHandler.class
})
class BitacoraSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private BitacoraService bitacoraService;

    @BeforeEach
    void prepararRespuestas() {
        when(bitacoraService.listar(
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(LocalDate.class),
                nullable(LocalDate.class),
                any(Pageable.class)
        )).thenReturn(new CustomPage<>());

        when(bitacoraService.registrar(any(BitacoraRequest.class)))
                .thenReturn(MessageResponse.setResponse(
                        true,
                        HttpStatus.CREATED,
                        "Registro de bitacora creado correctamente"
                ));
    }

    @Test
    void rechazaConsultaSinAutenticacion() throws Exception {
        mockMvc.perform(get("/api/bitacora"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void rechazaConsultaSinPermisoBitacora() throws Exception {
        mockMvc.perform(
                        get("/api/bitacora")
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
    void permiteConsultaConModuloBitacora() throws Exception {
        mockMvc.perform(
                        get("/api/bitacora")
                                .with(user("auditor")
                                        .authorities(
                                                new SimpleGrantedAuthority(
                                                        "MODULE_/bitacora"
                                                )
                                        ))
                )
                .andExpect(status().isOk());
    }

    @Test
    void permiteConsultaAlAdministrador() throws Exception {
        mockMvc.perform(
                        get("/api/bitacora")
                                .with(user("administrador")
                                        .authorities(
                                                new SimpleGrantedAuthority(
                                                        "ROLE_ADMIN"
                                                )
                                        ))
                )
                .andExpect(status().isOk());
    }

    @Test
    void rechazaRegistroSinAutenticacion() throws Exception {
        mockMvc.perform(
                        post("/api/bitacora")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "modulo": "Usuarios",
                                          "accion": "EDITAR",
                                          "detalle": "Prueba de auditoria",
                                          "registroId": "1"
                                        }
                                        """)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void permiteRegistroAUsuarioAutenticado() throws Exception {
        mockMvc.perform(
                        post("/api/bitacora")
                                .with(user("operador")
                                        .authorities(
                                                new SimpleGrantedAuthority(
                                                        "MODULE_/usuarios"
                                                )
                                        ))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "modulo": "Usuarios",
                                          "accion": "EDITAR",
                                          "detalle": "Prueba de auditoria",
                                          "registroId": "1"
                                        }
                                        """)
                )
                .andExpect(status().isCreated());
    }
}