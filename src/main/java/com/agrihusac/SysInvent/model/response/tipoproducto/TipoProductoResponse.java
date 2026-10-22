package com.agrihusac.SysInvent.model.response.tipoproducto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoProductoResponse {
    private Integer tipoProductoId;
    private String nombre;
    private String descripcion;
    private Boolean activo;
    private String fechaRegistro;
}
