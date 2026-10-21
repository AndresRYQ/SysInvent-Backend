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
public class ReporteIngresoListadoResponse {
    private Integer ingresoAlmacenId;
    private String numeroIngreso;
    private String fechaIngreso;
    private String proveedor;
    private String documento;
    private String tipoProducto;
    private String producto;
    private BigDecimal cantidad;
    private String unidadMedida;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private String estado;
}
