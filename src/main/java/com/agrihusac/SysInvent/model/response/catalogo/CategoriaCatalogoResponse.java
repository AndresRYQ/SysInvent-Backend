package com.agrihusac.SysInvent.model.response.catalogo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaCatalogoResponse {
    private Integer id;
    private String nombre;
    private String descripcion;
    private Boolean activo;
    private String fechaRegistro;
}
