package com.agrihusac.SysInvent.model.response.reporte;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Respuesta del reporte de Kardex: resumen, movimientos y stock actual.
 */
public record ReporteKardexResponse(
        Resumen resumen,
        List<Movimiento> movimientos,
        List<Stock> stock
) {

    public record Resumen(
            long totalMovimientos,
            long movimientosEntrada,
            long movimientosSalida,
            BigDecimal cantidadEntrada,
            BigDecimal cantidadSalida
    ) {
    }

    public record Movimiento(
            Long id,
            Integer productoId,
            String codigoProducto,
            String producto,
            String unidadMedida,
            LocalDateTime fechaMovimiento,
            /* dd/MM/yyyy */
            String fecha,
            /* HH:mm */
            String hora,
            String tipoMovimiento,
            String descripcionMovimiento,
            /* ENTRADA o SALIDA */
            String naturaleza,
            BigDecimal entrada,
            BigDecimal salida,
            BigDecimal saldoAnterior,
            BigDecimal saldoNuevo,
            BigDecimal precioUnitario,
            String numeroDocumento,
            String responsable
    ) {
    }

    public record Stock(
            Integer productoId,
            String codigo,
            String nombre,
            String unidadMedida,
            BigDecimal stockActual,
            BigDecimal stockMinimo,
            boolean requiereReposicion
    ) {
    }
}
