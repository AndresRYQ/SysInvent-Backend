package com.agrihusac.SysInvent.model.response.unidadmedida;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnidadMedidaResponse {
    private Integer unidadMedidaId;
    private String nombre;
    private String descripcion;
    private Boolean activo;
    private String fechaRegistro;
}
