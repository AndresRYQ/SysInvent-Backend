package com.agrihusac.SysInvent;

import com.agrihusac.SysInvent.api.ControlAlmacenController;
import com.agrihusac.SysInvent.api.KardexController;
import com.agrihusac.SysInvent.config.SecurityConfig;
import com.agrihusac.SysInvent.security.JwtAccessDeniedHandler;
import com.agrihusac.SysInvent.security.JwtAuthenticationEntryPoint;
import com.agrihusac.SysInvent.security.JwtAuthenticationFilter;
import com.agrihusac.SysInvent.service.JwtService;
import com.agrihusac.SysInvent.service.almacen.ControlAlmacenService;
import com.agrihusac.SysInvent.service.reporte.KardexService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {
        ControlAlmacenController.class,
        KardexController.class
})
@Import({
        SecurityConfig.class,
        JwtAuthenticationFilter.class,
        JwtAuthenticationEntryPoint.class,
        JwtAccessDeniedHandler.class
})
class ControlAlmacenSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private ControlAlmacenService controlAlmacenService;

    @MockBean
    private KardexService kardexService;

    @Test
    void rechazaExistenciasSinAutenticacion() throws Exception {
        mockMvc.perform(
                        get("/api/almacen/existencias")
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void rechazaUsuarioSinModuloControlAlmacen() throws Exception {
        mockMvc.perform(
                        get("/api/almacen/existencias")
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
    void permiteListarExistenciasConModuloControlAlmacen() throws Exception {
        mockMvc.perform(
                        get("/api/almacen/existencias")
                                .with(user("almacenero")
                                        .authorities(
                                                new SimpleGrantedAuthority(
                                                        "MODULE_/control-almacen"
                                                )
                                        ))
                )
                .andExpect(status().isOk());
    }

    @Test
    void permiteConsultarDetalleConModuloControlAlmacen() throws Exception {
        mockMvc.perform(
                        get("/api/almacen/existencias/1/detalle")
                                .with(user("almacenero")
                                        .authorities(
                                                new SimpleGrantedAuthority(
                                                        "MODULE_/control-almacen"
                                                )
                                        ))
                )
                .andExpect(status().isOk());
    }

    @Test
    void permiteConsultarKardexConModuloControlAlmacen() throws Exception {
        mockMvc.perform(
                        get("/api/kardex/stock")
                                .with(user("almacenero")
                                        .authorities(
                                                new SimpleGrantedAuthority(
                                                        "MODULE_/control-almacen"
                                                )
                                        ))
                )
                .andExpect(status().isOk());
    }

    @Test
    void permiteLecturasConModuloReporteKardex() throws Exception {
        var permisoReporte = new SimpleGrantedAuthority(
                "MODULE_/reportes/kardex"
        );

        mockMvc.perform(
                        get("/api/almacen/existencias")
                                .with(user("reporte-kardex")
                                        .authorities(permisoReporte))
                )
                .andExpect(status().isOk());

        mockMvc.perform(
                        get("/api/almacen/existencias/1/detalle")
                                .with(user("reporte-kardex")
                                        .authorities(permisoReporte))
                )
                .andExpect(status().isOk());

        mockMvc.perform(
                        get("/api/kardex/stock")
                                .with(user("reporte-kardex")
                                        .authorities(permisoReporte))
                )
                .andExpect(status().isOk());
    }

    @Test
    void rechazaEscrituraConModuloSoloReporteKardex() throws Exception {
        mockMvc.perform(
                        post("/api/almacen/existencias")
                                .with(user("reporte-kardex")
                                        .authorities(
                                                new SimpleGrantedAuthority(
                                                        "MODULE_/reportes/kardex"
                                                )
                                        ))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void permiteAccesoAlAdministrador() throws Exception {
        mockMvc.perform(
                        get("/api/almacen/existencias")
                                .with(user("administrador")
                                        .authorities(
                                                new SimpleGrantedAuthority(
                                                        "ROLE_ADMIN"
                                                )
                                        ))
                )
                .andExpect(status().isOk());
    }
}