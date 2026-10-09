package com.agrihusac.SysInvent.model.response.proveedor;

import java.time.LocalDate;

public record ProveedorResponse(
        Integer proveedorId,
        String ruc,
        String razonSocial,
        String correo,
        String telefono,
        String direccion,
        Boolean activo,
        LocalDate fechaRegistro
) {}
