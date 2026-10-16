package com.agrihusac.SysInvent.model.response.ingreso;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record IngresoAlmacenResponse(
        Integer id,
        String numeroIngreso,
        LocalDate fechaIngreso,
        Integer proveedorId,
        Integer contactoId,
        Integer tipoDocumentoId,
        String numeroDocumento,
        String estado,
        LocalDate fechaRegistro,
        List<Detalle> detalles
) {
    public record Detalle(
            Integer productoId,
            String codigoProducto,
            String nombreProducto,
            BigDecimal cantidad,
            BigDecimal precioUnitario,
            BigDecimal stockResultante
    ) {
    }
}package com.agrihusac.SysInvent.model.response.ingreso;

import com.fasterxml.jackson.databind.JsonNode;

/** Opaque placeholder; receipt and detail fields await the approved frontend contract. */
public record IngresoAlmacenResponse(JsonNode payload) {
}
