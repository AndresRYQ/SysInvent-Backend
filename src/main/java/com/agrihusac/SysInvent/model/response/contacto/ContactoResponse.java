package com.agrihusac.SysInvent.model.response.contacto;

import java.time.LocalDate;

public record ContactoResponse(
        Integer contactoId,
        Integer proveedorId,
        String nombreCompleto,
        String cargo,
        String telefono,
        String correo,
        Boolean activo,
        LocalDate fechaRegistro
) {}
