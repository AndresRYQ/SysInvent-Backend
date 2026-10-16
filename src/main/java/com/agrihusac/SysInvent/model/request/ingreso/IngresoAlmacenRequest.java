package com.agrihusac.SysInvent.model.request.ingreso;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record IngresoAlmacenRequest(
        @NotNull LocalDate fechaIngreso,
        @NotNull Integer proveedorId,
        @NotNull Integer contactoId,
        @NotNull Integer tipoDocumentoId,
        @NotBlank @Size(max = 50) String numeroDocumento,
        @Size(max = 500) String observacion,
        @NotEmpty List<@Valid @NotNull DetalleIngresoAlmacenRequest> detalles
) {
}package com.agrihusac.SysInvent.model.request.ingreso;

import com.fasterxml.jackson.databind.JsonNode;

/** Opaque placeholder; receipt and detail fields await the approved frontend contract. */
public record IngresoAlmacenRequest(JsonNode payload) {
}
