package com.agrihusac.SysInvent.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfilUsuarioResponse {

    private String codigo;
    private String usuario;
    private String dni;
    private String nombres;
    private String apePaterno;
    private String apeMaterno;
    private String nombreCompleto;
    private String correo;
    private String nombreRol;
    private Boolean estado;
    private String estadoDsc;
}
