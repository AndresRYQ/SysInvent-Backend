package com.agrihusac.SysInvent.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarPerfilRequest {

    @NotBlank(message = "{message.required}")
    @Size(max = 255, message = "{message.longitudmax}")
    private String nombres;

    @NotBlank(message = "{message.required}")
    @Size(max = 255, message = "{message.longitudmax}")
    private String apePaterno;

    @NotBlank(message = "{message.required}")
    @Size(max = 255, message = "{message.longitudmax}")
    private String apeMaterno;

    @NotBlank(message = "{message.required}")
    @Email(message = "{message.email}")
    @Size(max = 255, message = "{message.longitudmax}")
    private String email;
}
