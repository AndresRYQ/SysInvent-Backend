package com.agrihusac.SysInvent.service.reporte;

import com.agrihusac.SysInvent.model.response.reporte.ReporteProductoMasPedidoResponse;
import com.agrihusac.SysInvent.utils.CustomPage;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ReporteProductoMasPedidoService {

    CustomPage<ReporteProductoMasPedidoResponse> listar(String busqueda, Integer tipoProductoId,
                                                        Integer categoriaId, Integer destinoId,
                                                        LocalDate fechaDesde, LocalDate fechaHasta,
                                                        Pageable pageable);
}
