package com.agrihusac.SysInvent.model.request.ingreso;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

public record IngresoAlmacenRequest(
        @NotNull(message = "El proveedor es obligatorio")
        @Min(value = 1, message = "El proveedor es obligatorio")
        Integer proveedorId,
        @NotNull(message = "El contacto es obligatorio")
        @Min(value = 1, message = "El contacto es obligatorio")
        Integer contactoId,
        @NotNull(message = "El tipo de documento es obligatorio")
        @Min(value = 1, message = "El tipo de documento es obligatorio")
        Integer tipoDocumentoId,
        @NotBlank(message = "El numero de documento es obligatorio")
        @Size(max = 50, message = "Longitud maxima: 50")
        String numeroDocumento,
        LocalDate fechaIngreso,
        @Size(max = 500, message = "Longitud maxima: 500")
        String observacion,
        @NotEmpty(message = "El detalle del ingreso no puede estar vacio")
        List<@Valid DetalleIngresoAlmacenRequest> detalles
) {}
