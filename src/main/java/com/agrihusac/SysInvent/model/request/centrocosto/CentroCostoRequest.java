package com.agrihusac.SysInvent.model.request.centrocosto;

import jakarta.validation.constraints.*;

public record CentroCostoRequest(
        @NotBlank(message = "El campo es obligatorio")
        @Size(max = 150, message = "Longitud maxima: 150")
        String nombre,
        @Size(max = 255, message = "Longitud maxima: 255")
        String descripcion
) {}
