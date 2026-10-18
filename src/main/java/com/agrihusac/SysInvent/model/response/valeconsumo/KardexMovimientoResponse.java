package com.agrihusac.SysInvent.model.response.valeconsumo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record KardexMovimientoResponse(
        Long id,
        Integer productoId,
        String tipoMovimiento,
        LocalDateTime fechaMovimiento,
        BigDecimal cantidad,
        BigDecimal saldoAnterior,
        BigDecimal saldoNuevo,
        BigDecimal precioUnitario,
        Integer destinoId,
        Integer parteEquipoId
) {
}
