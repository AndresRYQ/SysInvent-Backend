package com.agrihusac.SysInvent.service.reporte;

import com.agrihusac.SysInvent.model.response.reporte.ReporteValeListadoResponse;
import com.agrihusac.SysInvent.utils.CustomPage;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ReporteValeService {

    CustomPage<ReporteValeListadoResponse> listar(String busqueda, Integer centroCostoId,
                                                   Integer destinoId, Integer parteEquipoId,
                                                   Integer tipoProductoId, Integer productoId,
                                                   String estado, LocalDate fechaDesde,
                                                   LocalDate fechaHasta, Pageable pageable);
}
