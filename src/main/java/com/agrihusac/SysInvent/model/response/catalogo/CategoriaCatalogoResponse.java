package com.agrihusac.SysInvent.model.response.catalogo;

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
public class CategoriaCatalogoResponse {
    private Integer id;
    private String nombre;
    private String descripcion;
    private Boolean activo;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaRegistro;
}
