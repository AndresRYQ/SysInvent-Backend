package com.agrihusac.SysInvent.model.response.proveedor;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record ProveedorResponse(
        Integer proveedorId,
        String ruc,
        String razonSocial,
        String correo,
        String telefono,
        String direccion,
        Boolean activo,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate fechaRegistro
) {}
