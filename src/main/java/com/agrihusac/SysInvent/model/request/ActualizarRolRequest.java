package com.agrihusac.SysInvent.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ActualizarRolRequest {

    @NotNull(message = "{message.required}")
    private Integer rolId;

    @NotBlank(message = "{message.required}")
    private String nombre;

    @NotBlank(message = "{message.required}")
    private String descripcion;

    @NotNull(message = "{message.required}")
    private List<@NotNull(message = "{message.required}") Integer> modulos;
}
