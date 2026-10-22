package com.agrihusac.SysInvent.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
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
    private String nombres;

    @NotBlank(message = "{message.required}")
    private String apePaterno;

    @NotBlank(message = "{message.required}")
    private String apeMaterno;

    @NotBlank(message = "{message.required}")
    @Email(message = "{message.email}")
    private String email;

    @NotBlank(message = "{message.required}")
    @Size(min = 12, max = 72, message = "La contraseña debe tener entre 12 y 72 caracteres")
    private String contrasena;

    @NotEmpty(message = "{message.required}")
    @Size(min = 1, max = 1, message = "El usuario debe tener exactamente un rol")
    private List<@NotNull(message = "{message.required}") Integer> rolesIds;
}
