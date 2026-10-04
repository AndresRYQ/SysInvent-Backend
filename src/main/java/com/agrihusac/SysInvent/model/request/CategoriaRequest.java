package com.agrihusac.SysInvent.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private String nombre;

    @NotBlank(message = "{message.required}")
    private String descripcion;
}
