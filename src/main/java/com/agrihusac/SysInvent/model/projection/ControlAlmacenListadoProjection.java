package com.agrihusac.SysInvent.model.projection;

import java.math.BigDecimal;

public interface ControlAlmacenListadoProjection {
    Integer getProductoId();
    String getCodigo();
    String getProducto();
    String getUnidadMedida();
    String getTipo();
    String getCategoria();
    String getProveedor();
    BigDecimal getEntradas();
    BigDecimal getSalidas();
    BigDecimal getStock();
    String getEstado();
}
