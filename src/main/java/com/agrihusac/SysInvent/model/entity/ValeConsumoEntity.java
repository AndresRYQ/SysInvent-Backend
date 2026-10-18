package com.agrihusac.SysInvent.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "vale_consumo")
@Getter
@Setter
@NoArgsConstructor
public class ValeConsumoEntity extends AuditoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vale_consumo_id")
    private Integer valeConsumoId;

    @Column(name = "numero_vale", nullable = false, unique = true, length = 30)
    private String numeroVale;

    @Column(name = "fecha_vale", nullable = false)
    private LocalDate fechaVale;

    @Column(name = "centro_costo_id", nullable = false)
    private Integer centroCostoId;

    @Column(name = "solicitante", nullable = false, length = 255)
    private String solicitante;

    @Column(name = "motivo", length = 500)
    private String motivo;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado;

    @OneToMany(mappedBy = "valeConsumo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("detalleValeConsumoId ASC")
    private List<DetalleValeConsumoEntity> detalles = new ArrayList<>();

    public void agregarDetalle(DetalleValeConsumoEntity detalle) {
        detalles.add(detalle);
        detalle.setValeConsumo(this);
    }

    public void limpiarDetalles() {
        detalles.clear();
    }
}
