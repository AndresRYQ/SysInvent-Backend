package com.agrihusac.SysInvent.model.response.parteequipo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParteEquipoResponse {
    private Integer parteEquipoId;
    private String codigo;
    private String nombre;
    private String descripcion;
    private Boolean activo;
    private String fechaRegistro;
}
