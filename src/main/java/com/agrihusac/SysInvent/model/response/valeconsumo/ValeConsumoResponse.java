package com.agrihusac.SysInvent.model.response.valeconsumo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ValeConsumoResponse(
        Integer id,
        String numeroVale,
        LocalDate fechaVale,
        Integer centroCostoId,
        String centroCosto,
        String solicitante,
        String motivo,
        String estado,
        LocalDate fechaRegistro,
        List<DetalleValeConsumoResponse> detalles,
        BigDecimal cantidadTotal,
        BigDecimal total,
        List<KardexMovimientoResponse> movimientosKardex
) {
}
