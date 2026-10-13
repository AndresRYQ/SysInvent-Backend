package com.agrihusac.SysInvent.model.response.producto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProductoResponse(
        Integer productoId,
        String codigo,
        String nombre,
        String descripcion,
        Integer tipoProductoId,
        Integer categoriaId,
        Integer unidadMedidaId,
        Integer proveedorId,
        BigDecimal stockActual,
        BigDecimal stockMinimo,
        BigDecimal precioUnitario,
        Boolean activo,
        LocalDate fechaRegistro
) {}
