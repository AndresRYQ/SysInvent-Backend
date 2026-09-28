package com.agrihusac.SysInvent.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ActualizarUsuarioRequest {

    @NotNull(message = "{message.required}")
    private Integer usuarioId;

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
