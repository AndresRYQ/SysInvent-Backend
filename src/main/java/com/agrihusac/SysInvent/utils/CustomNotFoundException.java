package com.agrihusac.SysInvent.utils;

public class CustomNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CustomNotFoundException() {
    }

    public CustomNotFoundException(String mensaje) {
        super(mensaje);
    }
}
