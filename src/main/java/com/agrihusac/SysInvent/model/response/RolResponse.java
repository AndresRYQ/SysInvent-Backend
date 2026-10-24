package com.agrihusac.SysInvent.model.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolResponse {

    private Integer rolId;
    private String nombre;
    private String descripcion;
    private Boolean activo;
    private Long usuarios;
    private List<Integer> modulos;
}