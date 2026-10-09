package com.agrihusac.SysInvent.model.response.destino;

import java.time.LocalDate;

public record DestinoResponse(
        Integer destinoId,
        String nombre,
        String descripcion,
        Boolean activo,
        LocalDate fechaRegistro
) {}
