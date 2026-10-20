package com.agrihusac.SysInvent.model.response.reporte;

import java.math.BigDecimal;

public record StockProductoResponse(
        Integer productoId,
        String codigo,
        String nombre,
        BigDecimal stockActual,
        BigDecimal stockMinimo,
        boolean requiereReposicion
) {
}