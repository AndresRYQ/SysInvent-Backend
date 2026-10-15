package com.agrihusac.SysInvent.model.projection;

public interface UsuarioProjection {

    Integer getUsuarioId();
    String getUsuario();
    String getNombreCompleto();
    String getCorreo();
    String getDescripcionRol();
    Boolean getEstado();

    String getEstadoDsc();
}
