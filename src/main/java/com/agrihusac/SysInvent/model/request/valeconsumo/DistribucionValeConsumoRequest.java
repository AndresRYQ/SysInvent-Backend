package com.agrihusac.SysInvent.model.request.valeconsumo;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DistribucionValeConsumoRequest(
        @NotNull Integer destinoId,
        Integer parteEquipoId,
        @NotNull @DecimalMin(value = "0.0001") @Digits(integer = 14, fraction = 4) BigDecimal cantidad
) {
}
