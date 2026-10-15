package com.agrihusac.SysInvent.model.request.valeconsumo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record DetalleValeConsumoRequest(
        @NotNull Integer productoId,
        @DecimalMin(value = "0.0") @Digits(integer = 14, fraction = 4)
        BigDecimal precioUnitario,
        @NotEmpty List<@Valid @NotNull DistribucionValeConsumoRequest> distribuciones
) {
}
