package com.agrihusac.SysInvent.model.request.parteequipo;

import jakarta.validation.constraints.*;

public record ParteEquipoRequest(
        @NotBlank(message = "El campo es obligatorio")
        @Size(max = 50, message = "Longitud maxima: 50")
        String codigo,
        @NotBlank(message = "El campo es obligatorio")
        @Size(max = 150, message = "Longitud maxima: 150")
        String nombre,
        @Size(max = 255, message = "Longitud maxima: 255")
        String descripcion
) {}
