package com.agrihusac.SysInvent.model.request.proveedor;

import jakarta.validation.constraints.*;

public record ProveedorRequest(
        @NotBlank(message = "El campo es obligatorio")
        @Pattern(regexp = "\\d{11}", message = "El RUC debe contener exactamente 11 digitos")
        @Size(max = 11, message = "Longitud maxima: 11")
        String ruc,
        @NotBlank(message = "El campo es obligatorio")
        @Size(max = 255, message = "Longitud maxima: 255")
        String razonSocial,
        @Size(max = 150, message = "Longitud maxima: 150")
        @Email(message = "El correo no es valido")
        String correo,
        @Pattern(regexp = "9\\d{8}", message = "El telefono debe tener 9 digitos y comenzar con 9")
        @Size(max = 9, message = "Longitud maxima: 9")
        String telefono,
        @Size(max = 255, message = "Longitud maxima: 255")
        String direccion
) {}
