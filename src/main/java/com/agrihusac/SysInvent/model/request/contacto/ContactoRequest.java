package com.agrihusac.SysInvent.model.request.contacto;

import jakarta.validation.constraints.*;

public record ContactoRequest(
        @NotNull(message = "El proveedor es obligatorio")
        @Positive(message = "El proveedor debe ser valido")
        Integer proveedorId,
        @NotBlank(message = "El campo es obligatorio")
        @Size(max = 255, message = "Longitud maxima: 255")
        String nombreCompleto,
        @Size(max = 150, message = "Longitud maxima: 150")
        String cargo,
        @Size(max = 30, message = "Longitud maxima: 30")
        String telefono,
        @Size(max = 150, message = "Longitud maxima: 150")
        @Email(message = "El correo no es valido")
        String correo
) {}
