package com.agrihusac.SysInvent.model.request.producto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductoRequest(
        @NotBlank(message = "El campo es obligatorio")
        @Size(max = 50, message = "Longitud maxima: 50")
        String codigo,
        @NotBlank(message = "El campo es obligatorio")
        @Size(max = 255, message = "Longitud maxima: 255")
        String nombre,
        @Size(max = 255, message = "Longitud maxima: 255")
        String descripcion,
        @NotNull(message = "El tipo de producto es obligatorio")
        @Positive(message = "El tipo de producto debe ser valido")
        Integer tipoProductoId,
        @NotNull(message = "La categoria es obligatoria")
        @Positive(message = "La categoria debe ser valida")
        Integer categoriaId,
        @NotNull(message = "La unidad de medida es obligatoria")
        @Positive(message = "La unidad de medida debe ser valida")
        Integer unidadMedidaId,
        @NotNull(message = "El proveedor es obligatorio")
        @Positive(message = "El proveedor debe ser valido")
        Integer proveedorId,
        @NotNull(message = "El stock actual es obligatorio")
        @DecimalMin(value = "0", message = "El stock actual no puede ser negativo")
        @Digits(integer = 14, fraction = 4, message = "Formato numerico invalido")
        BigDecimal stockActual,
        @NotNull(message = "El stock minimo es obligatorio")
        @DecimalMin(value = "0", message = "El stock minimo no puede ser negativo")
        @Digits(integer = 14, fraction = 4, message = "Formato numerico invalido")
        BigDecimal stockMinimo,
        @NotNull(message = "El precio unitario es obligatorio")
        @DecimalMin(value = "0", message = "El precio unitario no puede ser negativo")
        @Digits(integer = 14, fraction = 4, message = "Formato numerico invalido")
        BigDecimal precioUnitario
) {}
