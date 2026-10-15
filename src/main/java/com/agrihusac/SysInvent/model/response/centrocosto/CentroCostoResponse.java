package com.agrihusac.SysInvent.model.response.centrocosto;

import java.time.LocalDate;

public record CentroCostoResponse(
        Integer centroCostoId,
        String nombre,
        String descripcion,
        Boolean activo,
        LocalDate fechaRegistro
) {}
