package com.agrihusac.SysInvent.model.response.proveedor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProveedorResponse {
    private Integer proveedorId;
    private String ruc;
    private String razonSocial;
    private String correo;
    private String telefono;
    private String direccion;
    private Boolean activo;
    private String fechaRegistro;
}
