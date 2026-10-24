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
    private String codigo;
    private String usuario;
    private String dni;
    private String nombres;
    private String apePaterno;
    private String apeMaterno;
    private String nombreCompleto;
    private String correo;
    private Integer rolId;
    private String nombreRol;
    private String descripcionRol;
    private Boolean estado;
    private String estadoDsc;
}
