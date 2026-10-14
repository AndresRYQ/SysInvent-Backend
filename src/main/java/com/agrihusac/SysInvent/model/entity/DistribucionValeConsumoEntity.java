package com.agrihusac.SysInvent.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "distribucion_vale_consumo")
@Getter
@Setter
@NoArgsConstructor
public class DistribucionValeConsumoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "distribucion_vale_consumo_id")
    private Integer distribucionValeConsumoId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "detalle_vale_consumo_id", nullable = false)
    private DetalleValeConsumoEntity detalleValeConsumo;

    @Column(name = "destino_id", nullable = false)
    private Integer destinoId;

    @Column(name = "parte_equipo_id")
    private Integer parteEquipoId;

    @Column(name = "cantidad", nullable = false, precision = 18, scale = 4)
    private BigDecimal cantidad;
}
