package com.agrihusac.SysInvent.model.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ingreso_almacen")
@Getter
@Setter
@NoArgsConstructor
public class IngresoAlmacenEntity extends AuditoriaEntity {

    public static final String ESTADO_REGISTRADO = "REGISTRADO";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingreso_almacen_id")
    private Integer ingresoAlmacenId;

    @Column(name = "numero_ingreso", unique = true, length = 30, nullable = false)
    private String numeroIngreso;

    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "proveedor_id", nullable = false)
    private ProveedorEntity proveedor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contacto_id", nullable = false)
    private ContactoEntity contacto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_documento_id", nullable = false)
    private TipoDocumentoEntity tipoDocumento;

    @Column(name = "numero_documento", length = 50, nullable = false)
    private String numeroDocumento;

    @Column(name = "observacion", length = 500, nullable = true)
    private String observacion;

    @Column(name = "estado", length = 20, nullable = false)
    private String estado;

    @OneToMany(mappedBy = "ingreso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleIngresoAlmacenEntity> detalles = new ArrayList<>();
}
