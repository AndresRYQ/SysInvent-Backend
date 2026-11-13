package com.agrihusac.SysInvent.model.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface DashboardActividadProjection {
    LocalDateTime getFechaMovimiento();
    String getTipoMovimiento();
    String getDocumento();
    String getProducto();
    BigDecimal getCantidad();
    String getUsuarioResponsable();
    String getNombres();
    String getApePaterno();
    String getApeMaterno();
}
