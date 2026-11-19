package com.agrihusac.SysInvent.model.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface DashboardActividadProjection {

    LocalDateTime getFecha();
    String getTipo();
    String getDocumento();
    String getProducto();
    BigDecimal getCantidad();
    String getResponsable();
}