package com.agrihusac.SysInvent.model.projection;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ReporteValeListadoProjection {
    Integer getValeConsumoId();
    String getNumeroVale();
    LocalDate getFechaVale();
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
