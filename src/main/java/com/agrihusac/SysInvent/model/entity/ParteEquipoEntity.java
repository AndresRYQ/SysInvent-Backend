package com.agrihusac.SysInvent.model.entity;

import com.agrihusac.SysInvent.model.entity.AuditoriaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "parte_equipo")
@Getter
@Setter
@NoArgsConstructor
public class ParteEquipoEntity extends AuditoriaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parte_equipo_id")
    private Integer parteEquipoId;
    @Column(name = "codigo", unique = true, length = 50, nullable = false)
    private String codigo;
    @Column(name = "nombre", length = 150, nullable = false)
    private String nombre;
    @Column(name = "descripcion", length = 255, nullable = true)
    private String descripcion;

    @Column(name = "activo", nullable = false)
    private Boolean activo;
}
