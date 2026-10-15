package com.agrihusac.SysInvent.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {
    private Integer usuarioId;
    private String usuario;
    private String nombreCompleto;
    private String correo;
    private String descripcionRol;
    private Boolean estado;
    private String estadoDsc;
}
