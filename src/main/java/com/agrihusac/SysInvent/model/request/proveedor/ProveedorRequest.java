package com.agrihusac.SysInvent.model.request.proveedor;

import jakarta.validation.constraints.*;

public record ProveedorRequest(
        @NotBlank(message = "El campo es obligatorio")
        @Size(max = 20, message = "Longitud maxima: 20")
        String ruc,
        @NotBlank(message = "El campo es obligatorio")
        @Size(max = 255, message = "Longitud maxima: 255")
        String razonSocial,
        @Size(max = 150, message = "Longitud maxima: 150")
        @Email(message = "El correo no es valido")
        String correo,
        @Size(max = 30, message = "Longitud maxima: 30")
        String telefono,
        @Size(max = 255, message = "Longitud maxima: 255")
        String direccion
) {}
