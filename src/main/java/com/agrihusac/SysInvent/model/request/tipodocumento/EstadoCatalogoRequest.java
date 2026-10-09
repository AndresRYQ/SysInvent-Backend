package com.agrihusac.SysInvent.model.request.tipodocumento;

import jakarta.validation.constraints.NotNull;

public record EstadoCatalogoRequest(@NotNull(message = "El estado es obligatorio") Boolean activo) {}
