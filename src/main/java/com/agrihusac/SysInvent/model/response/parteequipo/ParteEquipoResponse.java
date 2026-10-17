package com.agrihusac.SysInvent.model.response.parteequipo;

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
public class ParteEquipoResponse {
    private Integer parteEquipoId;
    private String codigo;
    private String nombre;
    private String descripcion;
    private Boolean activo;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaRegistro;
}
