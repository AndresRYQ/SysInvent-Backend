package com.agrihusac.SysInvent.model.response.ingreso;

import java.math.BigDecimal;

public record IngresoAlmacenListadoResponse(
        Integer ingresoAlmacenId,
        String numeroIngreso,
        String fechaIngreso,
        String proveedor,
        String contacto,
        String documento,
        Integer productos,
        String cantidadIngresada,
        BigDecimal total,
        String estado
) {
}
