package com.agrihusac.SysInvent.model.response.almacen;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ControlAlmacenMovimientoResponse {

    private LocalDateTime fecha;
    private String movimiento;
    private String documento;
    private BigDecimal cantidad;
    private String unidadMedida;
    private String destino;
    private String parteEquipo;
    private String centroCosto;
    private String responsable;
    private String estado;
}
