package com.agrihusac.SysInvent.model.request.ingreso;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DetalleIngresoAlmacenRequest(
        @NotNull Integer productoId,
        @NotNull @DecimalMin(value = "0.0000", inclusive = false) @Digits(integer = 14, fraction = 4)
        BigDecimal cantidad,
        @NotNull @DecimalMin("0.0000") @Digits(integer = 14, fraction = 4) BigDecimal precioUnitario
) {
}package com.agrihusac.SysInvent.model.request.ingreso;

import com.fasterxml.jackson.databind.JsonNode;

/** Opaque placeholder; detail fields await the approved frontend contract. */
public record DetalleIngresoAlmacenRequest(JsonNode payload) {
}
