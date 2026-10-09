package com.agrihusac.SysInvent.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ModuloNoImplementadoException extends ResponseStatusException {

    public ModuloNoImplementadoException(String modulo) {
        // TODO: Replace each use with verified module behavior after its API contract is supplied.
        super(HttpStatus.NOT_IMPLEMENTED, "El módulo " + modulo + " aún no está implementado");
    }
}
