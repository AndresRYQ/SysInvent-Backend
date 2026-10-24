package com.agrihusac.SysInvent.event;

public record UsuarioRegistradoEvent(
        String destinatario,
        String nombres,
        String usuario,
        String contrasenaTemporal) {
}
