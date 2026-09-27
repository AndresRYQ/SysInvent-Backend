package com.agrihusac.SysInvent.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ErrorGenerico {

    private Integer tipMen;
    private String mensaje;
    private String codigo;
    private List<ErrorCampo> errores = Collections.emptyList();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String logerror;

    public ErrorGenerico(TypeMessage tipoMensaje, String mensaje, HttpStatus estado) {
        this.tipMen = tipoMensaje.getValue();
        this.mensaje = mensaje;
        this.codigo = String.valueOf(estado.value());
    }

    public ErrorGenerico(TypeMessage tipoMensaje, String mensaje, HttpStatus estado, String logerror) {
        this(tipoMensaje, mensaje, estado);
        this.logerror = logerror;
    }

    public ErrorGenerico(TypeMessage tipoMensaje, String mensaje, HttpStatus estado, List<ErrorCampo> errores) {
        this(tipoMensaje, mensaje, estado);
        this.errores = errores;
    }
}
