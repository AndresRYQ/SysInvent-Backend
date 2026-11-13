package com.agrihusac.SysInvent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agrihusac.SysInvent.model.projection.DashboardActividadProjection;
import com.agrihusac.SysInvent.model.projection.DashboardResumenProjection;
import com.agrihusac.SysInvent.model.response.dashboard.DashboardResponse;
import com.agrihusac.SysInvent.repository.DashboardRepository;
import com.agrihusac.SysInvent.service.dashboard.impl.DashboardServiceImpl;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    private static final ZoneId LIMA = ZoneId.of("America/Lima");

    @Mock
    private DashboardRepository dashboardRepository;

    private DashboardServiceImpl dashboardService;

    @BeforeEach
    void configurar() {
        // 13/11/2026 10:00 en Lima.
        Clock clock = Clock.fixed(Instant.parse("2026-11-13T15:00:00Z"), LIMA);
        dashboardService = new DashboardServiceImpl(dashboardRepository, clock);
    }

    @Test
    void calculaIndicadoresConRangosDelMesYDiaActual() {
        when(dashboardRepository.resumen(any(), any(), any(), any()))
                .thenReturn(resumen(12, 5, 3, 2, 4, 6, 1));
        when(dashboardRepository.actividadReciente(any())).thenReturn(List.of());

        DashboardResponse respuesta = dashboardService.obtener();

        verify(dashboardRepository).resumen(
                LocalDate.of(2026, 11, 1), LocalDate.of(2026, 12, 1),
                LocalDateTime.of(2026, 11, 13, 0, 0), LocalDateTime.of(2026, 11, 14, 0, 0));
        assertEquals(12L, respuesta.getCards().getProductosActivos());
        assertEquals(5L, respuesta.getCards().getStockCritico());
        assertEquals(3L, respuesta.getCards().getStockBajo());
        assertEquals(2L, respuesta.getCards().getProductosSinStock());
        assertEquals(4L, respuesta.getCards().getIngresosMes());
        assertEquals(6L, respuesta.getCards().getValesMes());
        assertEquals(1L, respuesta.getCards().getMovimientosHoy());
    }

    @Test
    void limitaActividadRecienteACincoRegistros() {
        when(dashboardRepository.resumen(any(), any(), any(), any()))
                .thenReturn(resumen(0, 0, 0, 0, 0, 0, 0));
        when(dashboardRepository.actividadReciente(any())).thenReturn(List.of());

        dashboardService.obtener();

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(dashboardRepository).actividadReciente(captor.capture());
        assertEquals(0, captor.getValue().getPageNumber());
        assertEquals(5, captor.getValue().getPageSize());
    }

    @Test
    void generaAlertasDeStockYUltimoMovimiento() {
        when(dashboardRepository.resumen(any(), any(), any(), any()))
                .thenReturn(resumen(10, 3, 2, 1, 0, 0, 0));
        when(dashboardRepository.actividadReciente(any())).thenReturn(List.of(
                actividad("SALIDA_VALE", "VAL-0001", "Ana", "Torres", "Ruiz", "atorres")));

        DashboardResponse respuesta = dashboardService.obtener();

        List<DashboardResponse.Alerta> alertas = respuesta.getSeguimiento();
        assertEquals(3, alertas.size());
        assertEquals("DANGER", alertas.get(0).getTipo());
        assertTrue(alertas.get(0).getDescripcion().startsWith("1 producto"));
        assertEquals("WARNING", alertas.get(1).getTipo());
        assertTrue(alertas.get(1).getDescripcion().startsWith("2 producto"));
        assertEquals("INFO", alertas.get(2).getTipo());
        assertEquals("VAL-0001 registrado el 12/11/2026.", alertas.get(2).getDescripcion());
    }

    @Test
    void informaCuandoNoHayAlertas() {
        when(dashboardRepository.resumen(any(), any(), any(), any()))
                .thenReturn(resumen(4, 0, 0, 0, 0, 0, 0));
        when(dashboardRepository.actividadReciente(any())).thenReturn(List.of());

        DashboardResponse respuesta = dashboardService.obtener();

        assertEquals(1, respuesta.getSeguimiento().size());
        assertEquals("INFO", respuesta.getSeguimiento().get(0).getTipo());
        assertTrue(respuesta.getActividadReciente().isEmpty());
    }

    @Test
    void mapeaActividadRecienteConResponsableYTipo() {
        when(dashboardRepository.resumen(any(), any(), any(), any()))
                .thenReturn(resumen(1, 0, 0, 0, 1, 0, 1));
        when(dashboardRepository.actividadReciente(any())).thenReturn(List.of(
                actividad("ENTRADA_INGRESO", "ING-0003", "Carlos", "Chinchay", null, "71477205"),
                actividad("SALIDA_VALE", "VAL-0002", null, null, null, "usuario.externo")));

        List<DashboardResponse.ActividadReciente> actividad = dashboardService.obtener().getActividadReciente();

        assertEquals("Ingreso", actividad.get(0).getTipo());
        assertEquals("Ingreso de almacén", actividad.get(0).getMovimiento());
        assertEquals("12/11/2026", actividad.get(0).getFecha());
        assertEquals("09:30", actividad.get(0).getHora());
        assertEquals("Carlos Chinchay", actividad.get(0).getResponsable());
        assertEquals("Vale", actividad.get(1).getTipo());
        assertEquals("usuario.externo", actividad.get(1).getResponsable());
    }

    private static DashboardResumenProjection resumen(long activos, long critico, long bajo, long sinStock,
                                                      long ingresos, long vales, long movimientos) {
        return new DashboardResumenProjection() {
            public Long getProductosActivos() { return activos; }
            public Long getStockCritico() { return critico; }
            public Long getStockBajo() { return bajo; }
            public Long getProductosSinStock() { return sinStock; }
            public Long getIngresosMes() { return ingresos; }
            public Long getValesMes() { return vales; }
            public Long getMovimientosHoy() { return movimientos; }
        };
    }

    private static DashboardActividadProjection actividad(String tipo, String documento, String nombres,
                                                          String apePaterno, String apeMaterno, String usuario) {
        return new DashboardActividadProjection() {
            public LocalDateTime getFechaMovimiento() { return LocalDateTime.of(2026, 11, 12, 9, 30); }
            public String getTipoMovimiento() { return tipo; }
            public String getDocumento() { return documento; }
            public String getProducto() { return "Guantes de nitrilo"; }
            public BigDecimal getCantidad() { return BigDecimal.TEN; }
            public String getUsuarioResponsable() { return usuario; }
            public String getNombres() { return nombres; }
            public String getApePaterno() { return apePaterno; }
            public String getApeMaterno() { return apeMaterno; }
        };
    }
}
