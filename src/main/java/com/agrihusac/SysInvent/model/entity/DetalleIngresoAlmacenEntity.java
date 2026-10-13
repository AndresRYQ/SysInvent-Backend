package com.agrihusac.SysInvent.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * El esquema actual de detalle_ingreso_almacen no incluye columnas de
 * auditoria, por lo que esta entidad no extiende AuditoriaEntity.
 */
@Entity
@Table(name = "detalle_ingreso_almacen")
@Getter
@Setter
@NoArgsConstructor
public class DetalleIngresoAlmacenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detalle_ingreso_almacen_id")
    private Integer detalleIngresoAlmacenId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingreso_almacen_id", nullable = false)
    private IngresoAlmacenEntity ingreso;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    private ProductoEntity producto;

    @Column(name = "cantidad", nullable = false, precision = 18, scale = 4)
    private BigDecimal cantidad;

    @Column(name = "precio_unitario", nullable = false, precision = 18, scale = 4)
    private BigDecimal precioUnitario;
}
