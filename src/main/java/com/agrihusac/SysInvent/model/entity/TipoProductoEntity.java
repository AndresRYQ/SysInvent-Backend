package com.agrihusac.SysInvent.model.entity;

import com.agrihusac.SysInvent.model.entity.AuditoriaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tipo_producto")
@Getter
@Setter
@NoArgsConstructor
public class TipoProductoEntity extends AuditoriaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tipo_producto_id")
    private Integer tipoProductoId;
    @Column(name = "nombre", unique = true, length = 100, nullable = false)
    private String nombre;
    @Column(name = "descripcion", length = 255, nullable = true)
    private String descripcion;

    @Column(name = "activo", nullable = false)
    private Boolean activo;
}
