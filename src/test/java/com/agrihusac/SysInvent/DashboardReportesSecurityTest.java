package com.agrihusac.SysInvent;

import com.agrihusac.SysInvent.api.DashboardController;
import com.agrihusac.SysInvent.api.ReporteIngresoController;
import com.agrihusac.SysInvent.api.ReporteKardexController;
import com.agrihusac.SysInvent.api.ReporteProductoMasPedidoController;
import com.agrihusac.SysInvent.api.ReporteValeController;
import com.agrihusac.SysInvent.config.SecurityConfig;
import com.agrihusac.SysInvent.security.JwtAccessDeniedHandler;
import com.agrihusac.SysInvent.security.JwtAuthenticationEntryPoint;
import com.agrihusac.SysInvent.security.JwtAuthenticationFilter;
import com.agrihusac.SysInvent.service.JwtService;
import com.agrihusac.SysInvent.service.dashboard.DashboardService;
import com.agrihusac.SysInvent.service.reporte.KardexService;
import com.agrihusac.SysInvent.service.reporte.ReporteIngresoService;
import com.agrihusac.SysInvent.service.reporte.ReporteProductoMasPedidoService;
import com.agrihusac.SysInvent.service.reporte.ReporteValeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {
        DashboardController.class,
        ReporteIngresoController.class,
        ReporteValeController.class,
        ReporteProductoMasPedidoController.class,
        ReporteKardexController.class
})
@Import({
        SecurityConfig.class,
        JwtAuthenticationFilter.class,
        JwtAuthenticationEntryPoint.class,
        JwtAccessDeniedHandler.class
})
class DashboardReportesSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private DashboardService dashboardService;

    @MockBean
    private ReporteIngresoService reporteIngresoService;

    @MockBean
    private ReporteValeService reporteValeService;

    @MockBean
    private ReporteProductoMasPedidoService reporteProductoMasPedidoService;

    @MockBean
    private KardexService kardexService;

    @Test
    void rechazaDashboardYReportesSinAutenticacion() throws Exception {
        mockMvc.perform(get("/api/dashboard"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/reportes/ingresos"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/reportes/vales"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/reportes/productos-mas-pedidos"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/reportes/kardex"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void permiteDashboardAUsuarioAutenticado() throws Exception {
        mockMvc.perform(get("/api/dashboard")
                        .with(user("usuario-dashboard")))
                .andExpect(status().isOk());
    }

    @Test
    void rechazaReportesConPermisoAjeno() throws Exception {
        var usuario = user("usuario-sin-reportes")
                .authorities(new SimpleGrantedAuthority("MODULE_/usuarios"));

        mockMvc.perform(get("/api/reportes/ingresos").with(usuario))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/reportes/vales").with(usuario))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/reportes/productos-mas-pedidos").with(usuario))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/reportes/kardex").with(usuario))
                .andExpect(status().isForbidden());
    }

    @Test
    void permiteReporteDeIngresosConSuModulo() throws Exception {
        mockMvc.perform(get("/api/reportes/ingresos")
                        .with(user("reporte-ingresos")
                                .authorities(new SimpleGrantedAuthority(
                                        "MODULE_/reportes/ingresos"
                                ))))
                .andExpect(status().isOk());
    }

    @Test
    void permiteReporteDeValesConSuModulo() throws Exception {
        mockMvc.perform(get("/api/reportes/vales")
                        .with(user("reporte-vales")
                                .authorities(new SimpleGrantedAuthority(
                                        "MODULE_/reportes/vales"
                                ))))
                .andExpect(status().isOk());
    }

    @Test
    void permiteProductosMasPedidosConSuModulo() throws Exception {
        mockMvc.perform(get("/api/reportes/productos-mas-pedidos")
                        .with(user("reporte-productos")
                                .authorities(new SimpleGrantedAuthority(
                                        "MODULE_/reportes/productos-mas-pedidos"
                                ))))
                .andExpect(status().isOk());
    }

    @Test
    void permiteKardexConAmbosModulosAutorizados() throws Exception {
        mockMvc.perform(get("/api/reportes/kardex")
                        .with(user("reporte-kardex")
                                .authorities(new SimpleGrantedAuthority(
                                        "MODULE_/reportes/kardex"
                                ))))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/reportes/kardex")
                        .with(user("control-almacen")
                                .authorities(new SimpleGrantedAuthority(
                                        "MODULE_/control-almacen"
                                ))))
                .andExpect(status().isOk());
    }
}