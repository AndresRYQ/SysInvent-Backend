package com.agrihusac.SysInvent.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarPerfilRequest {

    @NotBlank(message = "{message.required}")
    private String nombres;

    @NotBlank(message = "{message.required}")
    private String apePaterno;

    @NotBlank(message = "{message.required}")
    private String apeMaterno;

    @NotBlank(message = "{message.required}")
    @Email(message = "{message.email}")
    private String email;
}
