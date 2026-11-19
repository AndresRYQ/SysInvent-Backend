package com.agrihusac.SysInvent.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CambiarContrasenaRequest {

    @NotBlank(message = "La contrasena actual es obligatoria")
    @Size(max = 255, message = "{message.longitudmax}")
    private String contrasenaActual;

    @NotBlank(message = "La nueva contrasena es obligatoria")
    @Size(min = 6, max = 72, message = "La nueva contrasena debe tener entre 6 y 72 caracteres")
    private String nuevaContrasena;

    @NotBlank(message = "La confirmacion es obligatoria")
    @Size(max = 72, message = "{message.longitudmax}")
    private String confirmarContrasena;
}
