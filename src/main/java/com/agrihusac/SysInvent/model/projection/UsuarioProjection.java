package com.agrihusac.SysInvent.model.projection;

public interface UsuarioProjection {

    Integer getUsuarioId();
    String getUsuario();
    String getDni();
    String getNombres();
    String getApePaterno();
    String getApeMaterno();
    String getNombreCompleto();
    String getCorreo();
    Integer getRolId();
    String getNombreRol();
    String getDescripcionRol();
    Boolean getEstado();
    String getEstadoDsc();
}