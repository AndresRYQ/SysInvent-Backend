package com.agrihusac.SysInvent.model.response.ingreso;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record IngresoAlmacenResponse(
        Integer ingresoAlmacenId,
        String numeroIngreso,
        LocalDate fechaIngreso,
        LocalDate fechaRegistro,
        Integer proveedorId,
        String proveedor,
        Integer contactoId,
        String contacto,
        Integer tipoDocumentoId,
        String tipoDocumento,
        String numeroDocumento,
        String observacion,
        String estado,
        BigDecimal total,
        List<DetalleIngresoAlmacenResponse> detalles
) {
    @JsonProperty("id")
    public Integer id() {
        return ingresoAlmacenId;
    }
}
