package com.agrihusac.SysInvent.service.reporte.impl;

import com.agrihusac.SysInvent.model.projection.ReporteIngresoListadoProjection;
import com.agrihusac.SysInvent.model.response.reporte.ReporteIngresoListadoResponse;
import com.agrihusac.SysInvent.repository.IngresoAlmacenRepository;
import com.agrihusac.SysInvent.service.reporte.ReporteIngresoService;
import com.agrihusac.SysInvent.utils.CustomPage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReporteIngresoServiceImpl implements ReporteIngresoService {

    private final IngresoAlmacenRepository ingresoAlmacenRepository;

    @Override
    @Transactional(readOnly = true)
    public CustomPage<ReporteIngresoListadoResponse> listar(String busqueda, Integer proveedorId,
                                                             Integer tipoProductoId, Integer productoId,
                                                             String estado, LocalDate fechaDesde,
                                                             LocalDate fechaHasta, Pageable pageable) {
        Page<ReporteIngresoListadoResponse> page = ingresoAlmacenRepository.listarReporte(
                        busqueda == null ? "" : busqueda.trim(), proveedorId, tipoProductoId,
                        productoId, estado == null ? "" : estado.trim(), fechaDesde, fechaHasta, pageable)
                .map(this::convertir);
        return new CustomPage<>(page);
    }

    private ReporteIngresoListadoResponse convertir(ReporteIngresoListadoProjection projection) {
        return new ReporteIngresoListadoResponse(
                projection.getIngresoAlmacenId(), projection.getNumeroIngreso(),
                projection.getFechaIngreso(), projection.getProveedor(), projection.getDocumento(),
                projection.getTipoProducto(), projection.getProducto(), projection.getCantidad(),
                projection.getUnidadMedida(), projection.getPrecioUnitario(), projection.getSubtotal(),
                projection.getEstado());
    }
}
