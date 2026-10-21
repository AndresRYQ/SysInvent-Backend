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
public class ReporteValeListadoResponse {
    private Integer valeConsumoId;
    private String numeroVale;
    private String fechaVale;
    private String centroCosto;
    private String solicitante;
    private String codigoProducto;
    private String producto;
    private String destino;
    private String parteEquipo;
    private BigDecimal cantidad;
    private String unidadMedida;
    private BigDecimal precio;
    private BigDecimal subtotal;
    private String estado;
}
