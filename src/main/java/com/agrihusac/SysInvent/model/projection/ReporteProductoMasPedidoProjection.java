package com.agrihusac.SysInvent.model.projection;

import java.math.BigDecimal;

public interface ReporteProductoMasPedidoProjection {
    Integer getPosicion();
    Integer getProductoId();
    String getCodigoProducto();
    String getProducto();
    String getTipoProducto();
    String getCategoria();
    BigDecimal getCantidad();
    String getUnidadMedida();
    Long getVales();
    Long getDistribuciones();
    Long getDestinos();
    BigDecimal getParticipacion();
    BigDecimal getValorizado();
}
