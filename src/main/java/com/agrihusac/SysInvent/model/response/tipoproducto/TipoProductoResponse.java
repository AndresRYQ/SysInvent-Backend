package com.agrihusac.SysInvent.model.response.tipoproducto;

import java.time.LocalDate;

public record TipoProductoResponse(
        Integer tipoProductoId,
        String nombre,
        String descripcion,
        Boolean activo,
        LocalDate fechaRegistro
) {}
