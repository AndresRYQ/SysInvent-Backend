package com.agrihusac.SysInvent.model.response.parteequipo;

import java.time.LocalDate;

public record ParteEquipoResponse(
        Integer parteEquipoId,
        String codigo,
        String nombre,
        String descripcion,
        Boolean activo,
        LocalDate fechaRegistro
) {}
