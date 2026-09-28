package com.agrihusac.SysInvent.model.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolResponse {

    private Integer rolId;
    private String nombre;
    private String descripcion;
    private Boolean activo;
    private LocalDate fechaRegistro;
    private LocalDate fechaModificacion;
}
