package com.agrihusac.SysInvent.model.response.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private Cards cards;
    private List<Alerta> seguimiento;
    private List<ActividadReciente> actividadReciente;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Cards {
        private Long productosActivos;
        /** Productos activos con stock menor o igual al mínimo (incluye los que no tienen stock). */
        private Long stockCritico;
        /** Productos activos con stock mayor a cero y menor o igual al mínimo. */
        private Long stockBajo;
        /** Productos activos sin stock disponible. */
        private Long productosSinStock;
        private Long ingresosMes;
        private Long valesMes;
        private Long movimientosHoy;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Alerta {
        /** WARNING, DANGER o INFO. */
        private String tipo;
        private String titulo;
        private String descripcion;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActividadReciente {
        /** Fecha del movimiento en formato dd/MM/yyyy. */
        private String fecha;
        /** Hora del movimiento en formato HH:mm. */
        private String hora;
        /** "Ingreso" para entradas de stock y "Vale" para salidas. */
        private String tipo;
        /** Tipo de movimiento del Kardex (ENTRADA_INGRESO, SALIDA_VALE, etc.). */
        private String tipoMovimiento;
        /** Descripción legible del tipo de movimiento. */
        private String movimiento;
        private String documento;
        private String producto;
        private BigDecimal cantidad;
        private String responsable;
    }
}
