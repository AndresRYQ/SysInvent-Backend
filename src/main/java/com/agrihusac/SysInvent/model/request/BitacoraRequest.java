package com.agrihusac.SysInvent.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BitacoraRequest {
    @NotNull(message = "El rol es obligatorio")
    private Integer rolId;

    @NotBlank(message = "El módulo es obligatorio")
    @Size(max = 100, message = "El módulo no puede superar los 100 caracteres")
    private String modulo;

    @NotBlank(message = "La acción es obligatoria")
    @Pattern(regexp = "INICIO_SESION|CIERRE_SESION|CREAR|EDITAR|ELIMINAR|ACCESO_DENEGADO|BLOQUEO_LOGIN",
            message = "La acción no es válida")
    private String accion;

    @Size(max = 1000, message = "El detalle no puede superar los 1000 caracteres")
    private String detalle;

    @Size(max = 50, message = "El ID del registro no puede superar los 50 caracteres")
    private String registroId;
}
