package com.agrihusac.SysInvent.model.response.contacto;

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
public class ContactoResponse {
    private Integer contactoId;
    private Integer proveedorId;
    private String nombreCompleto;
    private String cargo;
    private String telefono;
    private String correo;
    private Boolean activo;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaRegistro;
}
