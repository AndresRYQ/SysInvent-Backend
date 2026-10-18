package com.agrihusac.SysInvent.model.response.producto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProductoResponse(
        Integer productoId,
        String codigo,
        String nombre,
        String descripcion,
        Integer tipoProductoId,
        String tipoProductoNombre,
        Integer categoriaId,
        String categoriaNombre,
        Integer unidadMedidaId,
        String unidadMedidaNombre,
        Integer proveedorId,
        String proveedorRazonSocial,
        BigDecimal stockActual,
        BigDecimal stockMinimo,
        BigDecimal precioUnitario,
        Boolean activo,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate fechaRegistro
) {}
