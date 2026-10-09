package com.agrihusac.SysInvent.model.request.unidadmedida;

import jakarta.validation.constraints.*;

public record UnidadMedidaRequest(
        @NotBlank(message = "El campo es obligatorio")
        @Size(max = 100, message = "Longitud maxima: 100")
        String nombre,
        @Size(max = 255, message = "Longitud maxima: 255")
        String descripcion
) {}
