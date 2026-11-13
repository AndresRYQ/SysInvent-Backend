package com.agrihusac.SysInvent.utils;

import java.util.List;
import java.util.stream.Stream;

/**
 * Tipos de movimiento del Kardex y su clasificación como entrada o salida.
 */
public final class TipoMovimientoKardex {

    public static final List<String> ENTRADAS = List.of("ENTRADA_INGRESO", "AJUSTE_ENTRADA");
    public static final List<String> SALIDAS = List.of("SALIDA_VALE", "AJUSTE_SALIDA", "ANULACION_ENTRADA");
    public static final List<String> TODOS = Stream.concat(ENTRADAS.stream(), SALIDAS.stream()).toList();

    private TipoMovimientoKardex() {
    }

    public static boolean esEntrada(String tipoMovimiento) {
        return ENTRADAS.contains(tipoMovimiento);
    }

    public static String describir(String tipoMovimiento) {
        if (tipoMovimiento == null) {
            return "Movimiento";
        }
        return switch (tipoMovimiento) {
            case "ENTRADA_INGRESO" -> "Ingreso de almacén";
            case "SALIDA_VALE" -> "Salida por vale";
            case "AJUSTE_ENTRADA" -> "Ajuste de entrada";
            case "AJUSTE_SALIDA" -> "Ajuste de salida";
            case "ANULACION_ENTRADA" -> "Anulación de ingreso";
            default -> tipoMovimiento;
        };
    }
}
