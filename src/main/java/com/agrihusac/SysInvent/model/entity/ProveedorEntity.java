package com.agrihusac.SysInvent.model.entity;

import com.agrihusac.SysInvent.model.entity.AuditoriaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "proveedor")
@Getter
@Setter
@NoArgsConstructor
public class ProveedorEntity extends AuditoriaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proveedor_id")
    private Integer proveedorId;
    @Column(name = "ruc", unique = true, length = 20, nullable = false)
    private String ruc;
    @Column(name = "razon_social", length = 255, nullable = false)
    private String razonSocial;
    @Column(name = "correo", length = 150, nullable = true)
    private String correo;
    @Column(name = "telefono", length = 30, nullable = true)
    private String telefono;
    @Column(name = "direccion", length = 255, nullable = true)
    private String direccion;

    @Column(name = "activo", nullable = false)
    private Boolean activo;
}
