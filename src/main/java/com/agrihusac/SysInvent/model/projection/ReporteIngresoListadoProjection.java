package com.agrihusac.SysInvent.model.projection;

import java.time.LocalDate;
import java.math.BigDecimal;

public interface ReporteIngresoListadoProjection {
    Integer getIngresoAlmacenId();
    String getNumeroIngreso();
    LocalDate getFechaIngreso();
    String getProveedor();
    String getDocumento();
    String getTipoProducto();
    String getProducto();
    BigDecimal getCantidad();
    String getUnidadMedida();
    BigDecimal getPrecioUnitario();
    BigDecimal getSubtotal();
    String getEstado();
}
