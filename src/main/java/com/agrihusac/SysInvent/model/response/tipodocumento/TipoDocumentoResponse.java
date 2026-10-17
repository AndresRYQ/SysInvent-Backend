package com.agrihusac.SysInvent.model.response.tipodocumento;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record TipoDocumentoResponse(
        Integer tipoDocumentoId,
        String nombre,
        String descripcion,
        Boolean activo,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate fechaRegistro
) {}
