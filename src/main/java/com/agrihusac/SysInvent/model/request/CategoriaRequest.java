package com.agrihusac.SysInvent.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CategoriaRequest {

    @NotNull(message = "{message.required}")
    private Integer categoriaId;

    @NotBlank(message = "{message.required}")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @NotBlank(message = "{message.required}")
    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    private String descripcion;
}
