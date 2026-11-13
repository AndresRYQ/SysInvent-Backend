package com.agrihusac.SysInvent.service.reporte;

import com.agrihusac.SysInvent.model.request.reporte.ReporteKardexFiltro;
import com.agrihusac.SysInvent.model.response.reporte.ReporteKardexResponse;

public interface ReporteKardexService {

    ReporteKardexResponse generar(ReporteKardexFiltro filtro);
}
