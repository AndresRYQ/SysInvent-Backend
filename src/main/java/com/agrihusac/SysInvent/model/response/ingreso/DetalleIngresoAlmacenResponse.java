package com.agrihusac.SysInvent.model.response.ingreso;

import java.math.BigDecimal;

public record DetalleIngresoAlmacenResponse(
        Integer detalleIngresoAlmacenId,
        Integer productoId,
        String codigoProducto,
        String nombreProducto,
        String unidadMedida,
        BigDecimal cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
) {}
