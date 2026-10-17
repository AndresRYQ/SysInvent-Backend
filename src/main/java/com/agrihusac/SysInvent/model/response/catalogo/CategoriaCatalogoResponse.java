package com.agrihusac.SysInvent.model.response.catalogo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record CategoriaCatalogoResponse(
        Integer id,
        String nombre,
        String descripcion,
        Boolean activo,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate fechaRegistro) {
}
