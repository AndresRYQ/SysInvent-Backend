package com.agrihusac.SysInvent;

import com.agrihusac.SysInvent.api.IngresoAlmacenController;
import com.agrihusac.SysInvent.config.SecurityConfig;
import com.agrihusac.SysInvent.model.request.ingreso.IngresoAlmacenRequest;
import com.agrihusac.SysInvent.security.JwtAccessDeniedHandler;
import com.agrihusac.SysInvent.security.JwtAuthenticationEntryPoint;
import com.agrihusac.SysInvent.security.JwtAuthenticationFilter;
import com.agrihusac.SysInvent.service.JwtService;
import com.agrihusac.SysInvent.service.ingreso.IngresoAlmacenService;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = IngresoAlmacenController.class)
@Import({
        SecurityConfig.class,
        JwtAuthenticationFilter.class,
        JwtAuthenticationEntryPoint.class,
        JwtAccessDeniedHandler.class
})
class IngresoAlmacenSecurityTest {

    private static final String INGRESO_JSON = """
            {
              "proveedorId": 1,
              "contactoId": 1,
              "tipoDocumentoId": 1,
              "numeroDocumento": "FAC-PRUEBA-001",
              "detalles": [
                {
                  "productoId": 1,
                  "cantidad": 1,
                  "precioUnitario": 10
                }
              ]
            }
            """;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private IngresoAlmacenService ingresoAlmacenService;

    @BeforeEach
    void configurarServicio() {
        ResponseEntity<Object> ingresoEncontrado =
                ResponseEntity.ok(Map.of("id", 1));

        ResponseEntity<Object> ingresoCreado =
                ResponseEntity.status(HttpStatus.CREATED)
                        .body(Map.of("id", 1));

        when(ingresoAlmacenService.obtener(1))
                .thenReturn(ingresoEncontrado);

        when(ingresoAlmacenService.registrar(
                any(IngresoAlmacenRequest.class)
        )).thenReturn(ingresoCreado);
    }

    @Test
    void rechazaConsultaSinAutenticacion() throws Exception {
        mockMvc.perform(
                        get("/api/ingresos-almacen/1")
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void rechazaUsuarioSinModuloDeIngresos() throws Exception {
        mockMvc.perform(
                        get("/api/ingresos-almacen/1")
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
    void permiteConsultarConModuloDeIngresos() throws Exception {
        mockMvc.perform(
                        get("/api/ingresos-almacen/1")
                                .with(user("almacenero")
                                        .authorities(
                                                new SimpleGrantedAuthority(
                                                        "MODULE_/ingresos-almacen"
                                                )
                                        ))
                )
                .andExpect(status().isOk());
    }

    @Test
    void permiteRegistrarConModuloDeIngresos() throws Exception {
        mockMvc.perform(
                        post("/api/ingresos-almacen")
                                .with(user("almacenero")
                                        .authorities(
                                                new SimpleGrantedAuthority(
                                                        "MODULE_/ingresos-almacen"
                                                )
                                        ))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(INGRESO_JSON)
                )
                .andExpect(status().isCreated());
    }

    @Test
    void permiteAccesoAlAdministrador() throws Exception {
        mockMvc.perform(
                        post("/api/ingresos-almacen")
                                .with(user("administrador")
                                        .authorities(
                                                new SimpleGrantedAuthority(
                                                        "ROLE_ADMIN"
                                                )
                                        ))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(INGRESO_JSON)
                )
                .andExpect(status().isCreated());
    }
}