package com.agrihusac.SysInvent.model.response.reporte;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReporteProductoMasPedidoResponse {
    private Integer posicion;
    private Integer productoId;
    private String codigoProducto;
    private String producto;
    private String tipoProducto;
    private String categoria;
    private BigDecimal cantidad;
    private String unidadMedida;
    private Long vales;
    private Long distribuciones;
    private Long destinos;
    private BigDecimal participacion;
    private BigDecimal valorizado;
}
