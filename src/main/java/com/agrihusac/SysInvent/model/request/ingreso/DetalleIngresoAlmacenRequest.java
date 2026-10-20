package com.agrihusac.SysInvent.model.request.ingreso;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record DetalleIngresoAlmacenRequest(
        @NotNull(message = "El producto es obligatorio")
        @Min(value = 1, message = "El producto es obligatorio")
        Integer productoId,
        @NotNull(message = "La cantidad es obligatoria")
        @Positive(message = "La cantidad debe ser mayor que cero")
        BigDecimal cantidad,
        @NotNull(message = "El precio unitario es obligatorio")
        @PositiveOrZero(message = "El precio unitario no puede ser negativo")
        BigDecimal precioUnitario
) {}
