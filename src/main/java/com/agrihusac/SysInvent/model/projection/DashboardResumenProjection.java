package com.agrihusac.SysInvent.model.projection;

public interface DashboardResumenProjection {
    Long getProductosActivos();
    Long getStockCritico();
    Long getProductosSinStock();
    Long getIngresosMes();
    Long getValesMes();
    Long getMovimientosHoy();
}
