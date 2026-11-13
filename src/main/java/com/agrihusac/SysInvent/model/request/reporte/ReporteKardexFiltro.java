package com.agrihusac.SysInvent.model.request.reporte;

import java.time.LocalDate;

/**
 * Filtros del reporte de Kardex.
 *
 * @param productoId     producto a consultar (opcional).
 * @param desde          fecha inicial inclusiva (opcional).
 * @param hasta          fecha final inclusiva (opcional).
 * @param tipoMovimiento ENTRADA, SALIDA o un tipo exacto del Kardex
 *                       (ENTRADA_INGRESO, SALIDA_VALE, AJUSTE_ENTRADA,
 *                       AJUSTE_SALIDA, ANULACION_ENTRADA). Opcional.
 */
public record ReporteKardexFiltro(
        Integer productoId,
        LocalDate desde,
        LocalDate hasta,
        String tipoMovimiento
) {
}
