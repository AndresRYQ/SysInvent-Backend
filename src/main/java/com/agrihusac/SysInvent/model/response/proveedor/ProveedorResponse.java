package com.agrihusac.SysInvent.model.response.proveedor;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaRegistro;
}
