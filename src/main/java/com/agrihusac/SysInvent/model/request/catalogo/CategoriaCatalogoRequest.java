package com.agrihusac.SysInvent.model.request.catalogo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaCatalogoRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
        String nombre,
        @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
        String descripcion) {
}
