package com.agrihusac.SysInvent.model.projection;

import java.math.BigDecimal;

public interface ReporteValeListadoProjection {
    Integer getValeConsumoId();
    String getNumeroVale();
    String getFechaVale();
    String getCentroCosto();
    String getSolicitante();
    String getCodigoProducto();
    String getProducto();
    String getDestino();
    String getParteEquipo();
    BigDecimal getCantidad();
    String getUnidadMedida();
    BigDecimal getPrecio();
    BigDecimal getSubtotal();
    String getEstado();
}
