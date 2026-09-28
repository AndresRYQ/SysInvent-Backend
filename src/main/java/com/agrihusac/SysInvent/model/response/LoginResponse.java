package com.agrihusac.SysInvent.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private Integer usuarioId;
    private String usuario;
    private String nombres;
    private String apePaterno;
    private String apeMaterno;
    private String email;
}
