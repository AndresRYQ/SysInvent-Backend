package com.agrihusac.SysInvent.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class UsuarioRequest {

    @NotBlank(message = "{message.required}")
    @Pattern(regexp = "\\d{8}", message = "{message.dni}")
    private String dni;

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

    @NotNull(message = "{message.required}")
    private Integer rolId;
}
