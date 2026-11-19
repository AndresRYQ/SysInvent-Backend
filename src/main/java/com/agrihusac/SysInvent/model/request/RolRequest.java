package com.agrihusac.SysInvent.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class RolRequest {

    @NotBlank(message = "{message.required}")
    @Size(max = 255, message = "{message.longitudmax}")
    private String nombre;

    @NotBlank(message = "{message.required}")
    @Size(max = 255, message = "{message.longitudmax}")
    private String descripcion;

    @NotNull(message = "{message.required}")
    private List<@NotNull(message = "{message.required}") Integer> modulos;
}
