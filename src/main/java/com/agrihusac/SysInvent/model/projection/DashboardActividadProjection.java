package com.agrihusac.SysInvent.model.projection;

import java.math.BigDecimal;

public interface DashboardActividadProjection {
    String getFecha();
    String getTipo();
    String getDocumento();
    String getProducto();
    BigDecimal getCantidad();
    String getResponsable();
}
