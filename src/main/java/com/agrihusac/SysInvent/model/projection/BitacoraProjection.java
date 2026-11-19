package com.agrihusac.SysInvent.model.projection;

import java.time.LocalDateTime;

public interface BitacoraProjection {

    Long getBitacoraId();
    LocalDateTime getFechaHora();
    String getNombreUsuario();
    String getNombreRol();
    String getModulo();
    String getAccion();
    String getDetalle();
    String getRegistroId();
}