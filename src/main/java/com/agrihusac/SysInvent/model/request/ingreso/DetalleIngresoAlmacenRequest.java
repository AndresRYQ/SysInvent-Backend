package com.agrihusac.SysInvent.model.request.ingreso;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DetalleIngresoAlmacenRequest(
        @NotNull(message = "El producto es obligatorio")
        @Min(value = 1, message = "El producto es obligatorio")
        Integer productoId,
        @NotNull(message = "La cantidad es obligatoria")
        @DecimalMin(value = "0.0000", inclusive = false, message = "La cantidad debe ser mayor que cero")
        @Digits(integer = 14, fraction = 4)
        BigDecimal cantidad,
        @NotNull(message = "El precio unitario es obligatorio")
        @DecimalMin(value = "0.0000", message = "El precio unitario no puede ser negativo")
        @Digits(integer = 14, fraction = 4)
        BigDecimal precioUnitario
) {
}
