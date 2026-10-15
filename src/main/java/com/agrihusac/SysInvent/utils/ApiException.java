package com.agrihusac.SysInvent.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
public class ApiException extends RuntimeException {

    private ErrorGenerico errorGenerico;

    public ApiException(TypeMessage tipoMensaje, String mensaje, HttpStatus estado) {
        super(mensaje);
        this.errorGenerico = new ErrorGenerico(tipoMensaje, mensaje, estado);
    }

    public ApiException(TypeMessage tipoMensaje, String mensaje, HttpStatus estado, String logerror) {
        super(mensaje);
        this.errorGenerico = new ErrorGenerico(tipoMensaje, mensaje, estado, logerror);
    }

    public ApiException(String mensaje, HttpStatus estado) {
        this(TypeMessage.DANGER, mensaje, estado);
    }

    public ApiException(TypeMessage tipoMensaje, String mensaje, HttpStatus estado, List<ErrorCampo> errores) {
        super(mensaje);
        this.errorGenerico = new ErrorGenerico(tipoMensaje, mensaje, estado, errores);
    }

    public ApiException(String mensaje, HttpStatus estado, List<ErrorCampo> errores) {
        this(TypeMessage.DANGER, mensaje, estado, errores);
    }
}
