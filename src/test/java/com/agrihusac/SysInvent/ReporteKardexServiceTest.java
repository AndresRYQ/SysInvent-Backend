package com.agrihusac.SysInvent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agrihusac.SysInvent.model.entity.KardexMovimientoEntity;
import com.agrihusac.SysInvent.model.entity.ProductoEntity;
import com.agrihusac.SysInvent.model.entity.UnidadMedidaEntity;
import com.agrihusac.SysInvent.model.entity.UsuarioEntity;
import com.agrihusac.SysInvent.model.request.reporte.ReporteKardexFiltro;
import com.agrihusac.SysInvent.model.response.reporte.ReporteKardexResponse;
import com.agrihusac.SysInvent.repository.KardexMovimientoRepository;
import com.agrihusac.SysInvent.repository.ProductoRepository;
import com.agrihusac.SysInvent.repository.UsuarioRepository;
import com.agrihusac.SysInvent.service.reporte.impl.ReporteKardexServiceImpl;
import com.agrihusac.SysInvent.utils.TipoMovimientoKardex;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class ReporteKardexServiceTest {

    @Mock
    private KardexMovimientoRepository kardexRepository;
    @Mock
    private ProductoRepository productoRepository;
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ReporteKardexServiceImpl service;

    @Test
    void resuelveGruposDeTipoDeMovimiento() {
        assertEquals(TipoMovimientoKardex.TODOS, ReporteKardexServiceImpl.resolverTipos(null));
        assertEquals(TipoMovimientoKardex.ENTRADAS, ReporteKardexServiceImpl.resolverTipos("entrada"));
        assertEquals(TipoMovimientoKardex.SALIDAS, ReporteKardexServiceImpl.resolverTipos("SALIDA"));
        assertEquals(List.of("SALIDA_VALE"), ReporteKardexServiceImpl.resolverTipos("SALIDA_VALE"));
        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> ReporteKardexServiceImpl.resolverTipos("OTRO"));
        assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
    }

    @Test
    void rechazaRangoDeFechasInvertido() {
        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> service.generar(new ReporteKardexFiltro(null,
                        LocalDate.of(2026, 11, 10), LocalDate.of(2026, 11, 1), null)));
        assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
    }

    @Test
    void generaMovimientosResumenYStock() {
        when(kardexRepository.buscarParaReporte(eq(1), eq(TipoMovimientoKardex.TODOS),
                eq(LocalDateTime.of(2026, 11, 1, 0, 0)), eq(LocalDateTime.of(2026, 11, 14, 0, 0))))
                .thenReturn(List.of(
                        movimiento(2L, "SALIDA_VALE", "4", "16", "12", "VAL-0001"),
                        movimiento(1L, "ENTRADA_INGRESO", "16", "0", "16", "ING-0001")));
        when(productoRepository.findAllConUnidadMedida()).thenReturn(List.of(
                producto(1, "Guantes", "12", "15", true),
                producto(2, "Mascarillas", "50", "10", true)));
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setUsuario("71477205");
        usuario.setNombres("Carlos");
        usuario.setApePaterno("Chinchay");
        when(usuarioRepository.findByUsuarioIn(any())).thenReturn(List.of(usuario));

        ReporteKardexResponse reporte = service.generar(new ReporteKardexFiltro(1,
                LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 13), null));

        assertEquals(2, reporte.resumen().totalMovimientos());
        assertEquals(1, reporte.resumen().movimientosEntrada());
        assertEquals(1, reporte.resumen().movimientosSalida());
        assertEquals(0, new BigDecimal("16").compareTo(reporte.resumen().cantidadEntrada()));
        assertEquals(0, new BigDecimal("4").compareTo(reporte.resumen().cantidadSalida()));

        ReporteKardexResponse.Movimiento salida = reporte.movimientos().get(0);
        assertEquals("SALIDA", salida.naturaleza());
        assertEquals("Salida por vale", salida.descripcionMovimiento());
        assertEquals(0, BigDecimal.ZERO.compareTo(salida.entrada()));
        assertEquals("Caja", salida.unidadMedida());
        assertEquals("Carlos Chinchay", salida.responsable());
        assertEquals("12/11/2026", salida.fecha());

        // El stock se limita al producto filtrado.
        assertEquals(1, reporte.stock().size());
        assertTrue(reporte.stock().get(0).requiereReposicion());
    }

    @Test
    void sinMovimientosDevuelveListasVacias() {
        when(kardexRepository.buscarParaReporte(isNull(), eq(TipoMovimientoKardex.ENTRADAS), isNull(), isNull()))
                .thenReturn(List.of());
        when(productoRepository.findAllConUnidadMedida()).thenReturn(List.of());

        ReporteKardexResponse reporte = service.generar(new ReporteKardexFiltro(null, null, null, "ENTRADA"));

        assertTrue(reporte.movimientos().isEmpty());
        assertEquals(0, reporte.resumen().totalMovimientos());
        verify(kardexRepository).buscarParaReporte(null, TipoMovimientoKardex.ENTRADAS, null, null);
    }

    private static KardexMovimientoEntity movimiento(Long id, String tipo, String cantidad,
                                                     String saldoAnterior, String saldoNuevo, String documento) {
        KardexMovimientoEntity movimiento = new KardexMovimientoEntity();
        movimiento.setKardexMovimientoId(id);
        movimiento.setProductoId(1);
        movimiento.setTipoMovimiento(tipo);
        movimiento.setFechaMovimiento(LocalDateTime.of(2026, 11, 12, 8, 15));
        movimiento.setCantidad(new BigDecimal(cantidad));
        movimiento.setSaldoAnterior(new BigDecimal(saldoAnterior));
        movimiento.setSaldoNuevo(new BigDecimal(saldoNuevo));
        movimiento.setPrecioUnitario(BigDecimal.ONE);
        movimiento.setNumeroDocumento(documento);
        movimiento.setUsuarioResponsable("71477205");
        return movimiento;
    }

    private static ProductoEntity producto(Integer id, String nombre, String stock, String minimo, boolean activo) {
        UnidadMedidaEntity unidad = new UnidadMedidaEntity();
        unidad.setNombre("Caja");
        ProductoEntity producto = new ProductoEntity();
        producto.setProductoId(id);
        producto.setCodigo("PRD-00" + id);
        producto.setNombre(nombre);
        producto.setStockActual(new BigDecimal(stock));
        producto.setStockMinimo(new BigDecimal(minimo));
        producto.setActivo(activo);
        producto.setUnidadMedida(unidad);
        return producto;
    }
}
