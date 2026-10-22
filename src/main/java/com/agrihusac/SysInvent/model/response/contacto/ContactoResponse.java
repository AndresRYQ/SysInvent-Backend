package com.agrihusac.SysInvent.model.response.contacto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactoResponse {
    private Integer contactoId;
    private Integer proveedorId;
    private String nombreCompleto;
    private String cargo;
    private String telefono;
    private String correo;
    private Boolean activo;
    private String fechaRegistro;
}
