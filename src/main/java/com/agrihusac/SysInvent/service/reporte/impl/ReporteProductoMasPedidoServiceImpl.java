package com.agrihusac.SysInvent.service.reporte.impl;

import com.agrihusac.SysInvent.model.projection.ReporteProductoMasPedidoProjection;
import com.agrihusac.SysInvent.model.response.reporte.ReporteProductoMasPedidoResponse;
import com.agrihusac.SysInvent.repository.ValeConsumoRepository;
import com.agrihusac.SysInvent.service.reporte.ReporteProductoMasPedidoService;
import com.agrihusac.SysInvent.utils.CustomPage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReporteProductoMasPedidoServiceImpl implements ReporteProductoMasPedidoService {

    private final ValeConsumoRepository valeConsumoRepository;

    @Override
    @Transactional(readOnly = true)
    public CustomPage<ReporteProductoMasPedidoResponse> listar(String busqueda, Integer tipoProductoId,
                                                                Integer categoriaId, Integer destinoId,
                                                                LocalDate fechaDesde, LocalDate fechaHasta,
                                                                Pageable pageable) {
        Page<ReporteProductoMasPedidoResponse> page = valeConsumoRepository.listarProductosMasPedidos(
                        busqueda == null ? "" : busqueda.trim(), tipoProductoId, categoriaId,
                        destinoId, fechaDesde, fechaHasta, pageable)
                .map(this::convertir);
        return new CustomPage<>(page);
    }

    private ReporteProductoMasPedidoResponse convertir(ReporteProductoMasPedidoProjection projection) {
        return new ReporteProductoMasPedidoResponse(
                projection.getPosicion(), projection.getProductoId(), projection.getCodigoProducto(),
                projection.getProducto(), projection.getTipoProducto(), projection.getCategoria(),
                projection.getCantidad(), projection.getUnidadMedida(), projection.getVales(),
                projection.getDistribuciones(), projection.getDestinos(), projection.getParticipacion(),
                projection.getValorizado());
    }
}
