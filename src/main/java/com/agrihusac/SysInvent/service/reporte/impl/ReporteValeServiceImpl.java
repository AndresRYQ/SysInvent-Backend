package com.agrihusac.SysInvent.service.reporte.impl;

import com.agrihusac.SysInvent.model.projection.ReporteValeListadoProjection;
import com.agrihusac.SysInvent.model.response.reporte.ReporteValeListadoResponse;
import com.agrihusac.SysInvent.repository.ValeConsumoRepository;
import com.agrihusac.SysInvent.service.reporte.ReporteValeService;
import com.agrihusac.SysInvent.utils.CustomPage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReporteValeServiceImpl implements ReporteValeService {

    private final ValeConsumoRepository valeConsumoRepository;

    @Override
    @Transactional(readOnly = true)
    public CustomPage<ReporteValeListadoResponse> listar(String busqueda, Integer centroCostoId,
                                                          Integer destinoId, Integer parteEquipoId,
                                                          Integer tipoProductoId, Integer productoId,
                                                          String estado, LocalDate fechaDesde,
                                                          LocalDate fechaHasta, Pageable pageable) {
        Page<ReporteValeListadoResponse> page = valeConsumoRepository.listarReporte(
                        busqueda == null ? "" : busqueda.trim(), centroCostoId, destinoId,
                        parteEquipoId, tipoProductoId, productoId,
                        estado == null ? "" : estado.trim(), fechaDesde, fechaHasta, pageable)
                .map(this::convertir);
        return new CustomPage<>(page);
    }

    private ReporteValeListadoResponse convertir(ReporteValeListadoProjection projection) {
        return new ReporteValeListadoResponse(
                projection.getValeConsumoId(), projection.getNumeroVale(), projection.getFechaVale(),
                projection.getCentroCosto(), projection.getSolicitante(), projection.getCodigoProducto(),
                projection.getProducto(), projection.getDestino(), projection.getParteEquipo(),
                projection.getCantidad(), projection.getUnidadMedida(), projection.getPrecio(),
                projection.getSubtotal(), projection.getEstado());
    }
}
