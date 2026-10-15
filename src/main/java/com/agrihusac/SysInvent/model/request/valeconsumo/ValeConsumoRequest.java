package com.agrihusac.SysInvent.model.request.valeconsumo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record ValeConsumoRequest(
        @NotNull LocalDate fechaVale,
        @NotNull Integer centroCostoId,
        @NotBlank @Size(min = 3, max = 120) String solicitante,
        @NotBlank @Size(min = 5, max = 500) String motivo,
        @NotEmpty List<@Valid @NotNull DetalleValeConsumoRequest> detalles
) {
}
