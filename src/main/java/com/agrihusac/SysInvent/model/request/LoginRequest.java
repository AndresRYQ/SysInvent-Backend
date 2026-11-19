package com.agrihusac.SysInvent.model.request;

import jakarta.validation.constraints.NotBlank;
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
public class LoginRequest {

    @NotBlank(message = "{message.required}")
    @Size(max = 255, message = "{message.longitudmax}")
    private String usuario;

    @NotBlank(message = "{message.required}")
    @Size(max = 255, message = "{message.longitudmax}")
    private String contrasena;
}
