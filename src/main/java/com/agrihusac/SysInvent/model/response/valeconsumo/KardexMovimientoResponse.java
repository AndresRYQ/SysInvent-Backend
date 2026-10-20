package com.agrihusac.SysInvent.model.response.valeconsumo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record KardexMovimientoResponse(
        Long id,
        Integer productoId,
        String codigoProducto,
        String nombreProducto,
        String tipoMovimiento,
        LocalDateTime fechaMovimiento,
        BigDecimal cantidad,
        BigDecimal saldoAnterior,
        BigDecimal saldoNuevo,
        BigDecimal precioUnitario,
        String usuarioResponsable,
        String numeroDocumento,
        Integer ingresoAlmacenId,
        Integer valeConsumoId,
        Integer centroCostoId,
        Integer destinoId,
        Integer parteEquipoId
) {
}
