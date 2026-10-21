package com.agrihusac.SysInvent.model.response.valeconsumo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValeConsumoListadoResponse {
    private Integer id;
    private String numeroVale;
    private LocalDate fechaVale;
    private String centroCosto;
    private String solicitante;
    private Long productos;
    private Long destinos;
    private BigDecimal cantidadTotal;
    private BigDecimal total;
    private String estado;
}
