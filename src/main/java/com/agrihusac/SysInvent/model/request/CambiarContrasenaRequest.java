package com.agrihusac.SysInvent.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CambiarContrasenaRequest {

    @NotBlank(message = "La contrasena actual es obligatoria")
    private String contrasenaActual;

    @NotBlank(message = "La nueva contrasena es obligatoria")
    @Size(min = 6, message = "La nueva contrasena debe tener al menos 6 caracteres")
    private String nuevaContrasena;

    @NotBlank(message = "La confirmacion es obligatoria")
    private String confirmarContrasena;
}
