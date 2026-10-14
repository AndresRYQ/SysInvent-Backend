package com.agrihusac.SysInvent.model.response.valeconsumo;

import java.math.BigDecimal;
import java.util.List;

public record DetalleValeConsumoResponse(
        Integer id,
        Integer productoId,
        String codigoProducto,
        String producto,
        Integer unidadMedidaId,
        String unidadMedida,
        BigDecimal precioUnitario,
        BigDecimal stockActual,
        BigDecimal cantidad,
        BigDecimal subtotal,
        List<DistribucionValeConsumoResponse> distribuciones
) {
}
