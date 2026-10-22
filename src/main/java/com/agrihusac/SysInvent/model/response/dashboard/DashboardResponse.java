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
        private Long stockCritico;
        private Long ingresosMes;
        private Long valesMes;
        private Long movimientosHoy;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Alerta {
        private String tipo;
        private String titulo;
        private String descripcion;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActividadReciente {
        private String fecha;
        private String tipo;
        private String documento;
        private String producto;
        private BigDecimal cantidad;
        private String responsable;
    }
}
