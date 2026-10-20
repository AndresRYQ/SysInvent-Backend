package com.agrihusac.SysInvent.model.request.producto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductoRequest(
        @NotBlank(message = "El código es obligatorio")
        @Size(max = 50, message = "El código no puede exceder 50 caracteres")
        String codigo,
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 255, message = "El nombre no puede exceder 255 caracteres")
        String nombre,
        @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
        String descripcion,
        @NotNull(message = "El tipo de producto es obligatorio")
        @Positive(message = "El tipo de producto debe ser válido")
        Integer tipoProductoId,
        @NotNull(message = "La categoría es obligatoria")
        @Positive(message = "La categoría debe ser válida")
        Integer categoriaId,
        @NotNull(message = "La unidad de medida es obligatoria")
        @Positive(message = "La unidad de medida debe ser válida")
        Integer unidadMedidaId,
        @NotNull(message = "El proveedor es obligatorio")
        @Positive(message = "El proveedor debe ser válido")
        Integer proveedorId,
        @DecimalMin(value = "0.0000", inclusive = true, message = "El stock actual no puede ser negativo")
        BigDecimal stockActual,
        @NotNull(message = "El stock mínimo es obligatorio")
        @DecimalMin(value = "0.0000", inclusive = true, message = "El stock mínimo no puede ser negativo")
        BigDecimal stockMinimo,
        @NotNull(message = "El precio unitario es obligatorio")
        @DecimalMin(value = "0.0000", inclusive = true, message = "El precio unitario no puede ser negativo")
        BigDecimal precioUnitario
) {}
