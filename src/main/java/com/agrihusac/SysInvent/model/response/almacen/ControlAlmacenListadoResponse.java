package com.agrihusac.SysInvent.model.response.almacen;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ControlAlmacenListadoResponse {

    private Integer productoId;
    private String codigo;
    private String producto;
    private String unidadMedida;
    private String tipo;
    private String categoria;
    private String proveedor;
    private BigDecimal entradas;
    private BigDecimal salidas;
    private BigDecimal stock;
    private String estado;
}
