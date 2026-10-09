package com.agrihusac.SysInvent.model.entity;

import com.agrihusac.SysInvent.model.entity.AuditoriaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "destino")
@Getter
@Setter
@NoArgsConstructor
public class DestinoEntity extends AuditoriaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "destino_id")
    private Integer destinoId;
    @Column(name = "nombre", unique = true, length = 150, nullable = false)
    private String nombre;
    @Column(name = "descripcion", length = 255, nullable = true)
    private String descripcion;

    @Column(name = "activo", nullable = false)
    private Boolean activo;
}
