package com.agrihusac.SysInvent.model.projection;

import java.math.BigDecimal;

public interface IngresoAlmacenListadoProjection {
    Integer getIngresoAlmacenId();
    String getNumeroIngreso();
    String getFechaIngreso();
    String getProveedor();
    String getContacto();
    String getDocumento();
    Integer getProductos();
    String getCantidadIngresada();
    BigDecimal getTotal();
    String getEstado();
}
