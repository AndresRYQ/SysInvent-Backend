package com.agrihusac.SysInvent.model.response.destino;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record DestinoResponse(
        Integer destinoId,
        String nombre,
        String descripcion,
        Boolean activo,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate fechaRegistro
) {}
