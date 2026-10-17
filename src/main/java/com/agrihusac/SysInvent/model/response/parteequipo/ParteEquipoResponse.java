package com.agrihusac.SysInvent.model.response.parteequipo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record ParteEquipoResponse(
        Integer parteEquipoId,
        String codigo,
        String nombre,
        String descripcion,
        Boolean activo,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate fechaRegistro
) {}
