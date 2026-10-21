package com.agrihusac.SysInvent.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "numero_ingreso", nullable = false, unique = true, length = 30)
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

    @Column(name = "numero_documento", nullable = false, length = 50)
    private String numeroDocumento;

    @Column(name = "observacion", length = 500)
    private String observacion;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado;

    @OneToMany(mappedBy = "ingreso", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("detalleIngresoAlmacenId ASC")
    private List<DetalleIngresoAlmacenEntity> detalles = new ArrayList<>();

    public void agregarDetalle(DetalleIngresoAlmacenEntity detalle) {
        detalles.add(detalle);
        detalle.setIngreso(this);
    }
}
