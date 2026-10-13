package com.agrihusac.SysInvent.model.response.unidadmedida;

import java.time.LocalDate;

public record UnidadMedidaResponse(
        Integer unidadMedidaId,
        String nombre,
        String descripcion,
        Boolean activo,
        LocalDate fechaRegistro
) {}
