package com.agrihusac.SysInvent.model.response.unidadmedida;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record UnidadMedidaResponse(
        Integer unidadMedidaId,
        String nombre,
        String descripcion,
        Boolean activo,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate fechaRegistro
) {}
