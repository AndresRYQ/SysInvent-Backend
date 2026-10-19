package com.agrihusac.SysInvent.model.response.centrocosto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CentroCostoResponse {
    private Integer centroCostoId;
    private String nombre;
    private String descripcion;
    private Boolean activo;
    private String fechaRegistro;
}
