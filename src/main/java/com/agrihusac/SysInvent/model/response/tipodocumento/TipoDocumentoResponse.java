package com.agrihusac.SysInvent.model.response.tipodocumento;

import java.time.LocalDate;

public record TipoDocumentoResponse(
        Integer tipoDocumentoId,
        String nombre,
        String descripcion,
        Boolean activo,
        LocalDate fechaRegistro
) {}
