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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "detalle_vale_consumo")
@Getter
@Setter
@NoArgsConstructor
public class DetalleValeConsumoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detalle_vale_consumo_id")
    private Integer detalleValeConsumoId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vale_consumo_id", nullable = false)
    private ValeConsumoEntity valeConsumo;

    @Column(name = "producto_id", nullable = false)
    private Integer productoId;

    @Column(name = "precio_unitario", nullable = false, precision = 18, scale = 4)
    private BigDecimal precioUnitario;

    @OneToMany(mappedBy = "detalleValeConsumo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("distribucionValeConsumoId ASC")
    private List<DistribucionValeConsumoEntity> distribuciones = new ArrayList<>();

    public void agregarDistribucion(DistribucionValeConsumoEntity distribucion) {
        distribuciones.add(distribucion);
        distribucion.setDetalleValeConsumo(this);
    }
}
