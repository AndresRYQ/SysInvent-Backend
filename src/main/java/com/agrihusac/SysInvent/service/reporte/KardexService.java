package com.agrihusac.SysInvent.service.reporte;

import com.agrihusac.SysInvent.model.response.reporte.StockProductoResponse;
import com.agrihusac.SysInvent.model.response.valeconsumo.KardexMovimientoResponse;

import java.time.LocalDate;
import java.util.List;

public interface KardexService {

    List<KardexMovimientoResponse> buscar(Integer productoId, LocalDate desde, LocalDate hasta, String tipoMovimiento);

    List<StockProductoResponse> consultarStock(boolean soloMinimo);
}