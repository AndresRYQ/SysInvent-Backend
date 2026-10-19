package com.agrihusac.SysInvent.model.response.producto;

import java.math.BigDecimal;

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
        String fechaRegistro
) {}
