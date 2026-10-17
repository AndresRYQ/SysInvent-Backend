package com.agrihusac.SysInvent.model.response.contacto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record ContactoResponse(
        Integer contactoId,
        Integer proveedorId,
        String nombreCompleto,
        String cargo,
        String telefono,
        String correo,
        Boolean activo,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate fechaRegistro
) {}
