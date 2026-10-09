package com.agrihusac.SysInvent.model.entity;

import com.agrihusac.SysInvent.model.entity.AuditoriaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contacto")
@Getter
@Setter
@NoArgsConstructor
public class ContactoEntity extends AuditoriaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contacto_id")
    private Integer contactoId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "proveedor_id", nullable = false)
    private ProveedorEntity proveedor;
    @Column(name = "nombre_completo", length = 255, nullable = false)
    private String nombreCompleto;
    @Column(name = "cargo", length = 150, nullable = true)
    private String cargo;
    @Column(name = "telefono", length = 30, nullable = true)
    private String telefono;
    @Column(name = "correo", length = 150, nullable = true)
    private String correo;

    @Column(name = "activo", nullable = false)
    private Boolean activo;
}
