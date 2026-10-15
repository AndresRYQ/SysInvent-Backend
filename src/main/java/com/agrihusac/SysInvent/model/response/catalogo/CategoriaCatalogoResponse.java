package com.agrihusac.SysInvent.model.response.catalogo;

import java.time.LocalDate;

public record CategoriaCatalogoResponse(
        Integer id,
        String nombre,
        String descripcion,
        Boolean activo,
        LocalDate fechaRegistro) {
}
