package com.agrihusac.SysInvent.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class FechaUtils {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private FechaUtils() {
    }

    public static String formatear(LocalDate fecha) {
        return fecha == null ? null : fecha.format(FORMATO_FECHA);
    }
}
