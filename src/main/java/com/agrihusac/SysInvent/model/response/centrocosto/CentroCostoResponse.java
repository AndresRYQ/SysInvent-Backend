package com.agrihusac.SysInvent.model.response.centrocosto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record CentroCostoResponse(
        Integer centroCostoId,
        String nombre,
        String descripcion,
        Boolean activo,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate fechaRegistro
) {}
