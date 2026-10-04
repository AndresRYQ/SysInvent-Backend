package com.agrihusac.SysInvent.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaResponse {

    private Integer categoriaId;
    private String nombre;
    private String descripcion;
}
