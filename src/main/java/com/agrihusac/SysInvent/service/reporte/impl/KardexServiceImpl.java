package com.agrihusac.SysInvent.service.reporte.impl;

import com.agrihusac.SysInvent.model.entity.KardexMovimientoEntity;
import com.agrihusac.SysInvent.model.entity.ProductoEntity;
import com.agrihusac.SysInvent.model.response.reporte.StockProductoResponse;
import com.agrihusac.SysInvent.model.response.valeconsumo.KardexMovimientoResponse;
import com.agrihusac.SysInvent.repository.KardexMovimientoRepository;
import com.agrihusac.SysInvent.repository.ProductoRepository;
import com.agrihusac.SysInvent.service.reporte.KardexService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KardexServiceImpl implements KardexService {

    private final KardexMovimientoRepository kardexRepository;
    private final ProductoRepository productoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<KardexMovimientoResponse> buscar(
            Integer productoId, LocalDate desde, LocalDate hasta, String tipoMovimiento) {
        return kardexRepository.buscarMovimientos(
                        productoId,
                        tipoMovimiento == null || tipoMovimiento.isBlank() ? null : tipoMovimiento.trim(),
                        desde == null ? null : desde.atStartOfDay(),
                        hasta == null ? null : hasta.plusDays(1).atStartOfDay())
                .stream()
                .map(this::convertir)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockProductoResponse> consultarStock(boolean soloMinimo) {
        return productoRepository.findAll().stream()
                .filter(producto -> Boolean.TRUE.equals(producto.getActivo()))
                .filter(producto -> !soloMinimo || producto.getStockActual().compareTo(producto.getStockMinimo()) <= 0)
                .sorted((a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()))
                .map(this::convertirStock)
                .toList();
    }

    private KardexMovimientoResponse convertir(KardexMovimientoEntity movimiento) {
        ProductoEntity producto = productoRepository.findById(movimiento.getProductoId()).orElse(null);
        return new KardexMovimientoResponse(
                movimiento.getKardexMovimientoId(),
                movimiento.getProductoId(),
                producto == null ? null : producto.getCodigo(),
                producto == null ? null : producto.getNombre(),
                movimiento.getTipoMovimiento(),
                movimiento.getFechaMovimiento(),
                movimiento.getCantidad(),
                movimiento.getSaldoAnterior(),
                movimiento.getSaldoNuevo(),
                movimiento.getPrecioUnitario(),
                movimiento.getUsuarioResponsable(),
                movimiento.getNumeroDocumento(),
                movimiento.getIngresoAlmacenId(),
                movimiento.getValeConsumoId(),
                movimiento.getCentroCostoId(),
                movimiento.getDestinoId(),
                movimiento.getParteEquipoId());
    }

    private StockProductoResponse convertirStock(ProductoEntity producto) {
        return new StockProductoResponse(
                producto.getProductoId(), producto.getCodigo(), producto.getNombre(),
                producto.getStockActual(), producto.getStockMinimo(),
                producto.getStockActual().compareTo(producto.getStockMinimo()) <= 0);
    }
}