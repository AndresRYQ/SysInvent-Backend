package com.agrihusac.SysInvent.common;

import java.math.BigDecimal;
import java.util.List;

public class StockInsuficienteException extends RuntimeException {

    private final List<ProductoSinStock> productos;

    public StockInsuficienteException(List<ProductoSinStock> productos) {
        super("No hay stock suficiente para completar la salida.");
        this.productos = List.copyOf(productos);
    }

    public List<ProductoSinStock> getProductos() {
        return productos;
    }

    public record ProductoSinStock(
            Integer productoId,
            String producto,
            BigDecimal cantidadSolicitada,
            BigDecimal cantidadDisponible
    ) {
    }
}
