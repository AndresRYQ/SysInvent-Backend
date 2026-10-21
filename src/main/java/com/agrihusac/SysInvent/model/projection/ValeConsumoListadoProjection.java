package com.agrihusac.SysInvent.model.projection;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ValeConsumoListadoProjection {
    Integer getId();
    String getNumeroVale();
    LocalDate getFechaVale();
    String getCentroCosto();
    String getSolicitante();
    Long getProductos();
    Long getDestinos();
    BigDecimal getCantidadTotal();
    BigDecimal getTotal();
    String getEstado();
}
