package com.agrihusac.SysInvent.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "kardex_movimiento")
@Getter
@Setter
@NoArgsConstructor
public class KardexMovimientoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kardex_movimiento_id")
    private Long kardexMovimientoId;

    @Column(name = "vale_consumo_id")
    private Integer valeConsumoId;

    @Column(name = "ingreso_almacen_id")
    private Integer ingresoAlmacenId;

    @Column(name = "usuario_responsable", nullable = false, length = 100)
    private String usuarioResponsable;

    @Column(name = "producto_id", nullable = false)
    private Integer productoId;

    @Column(name = "tipo_movimiento", nullable = false, length = 30)
    private String tipoMovimiento;

    @Column(name = "fecha_movimiento", nullable = false)
    private LocalDateTime fechaMovimiento;

    @Column(name = "cantidad", nullable = false, precision = 18, scale = 4)
    private BigDecimal cantidad;

    @Column(name = "saldo_anterior", nullable = false, precision = 18, scale = 4)
    private BigDecimal saldoAnterior;

    @Column(name = "saldo_nuevo", nullable = false, precision = 18, scale = 4)
    private BigDecimal saldoNuevo;

    @Column(name = "precio_unitario", nullable = false, precision = 18, scale = 4)
    private BigDecimal precioUnitario;

    @Column(name = "centro_costo_id")
    private Integer centroCostoId;

    @Column(name = "destino_id")
    private Integer destinoId;

    @Column(name = "parte_equipo_id")
    private Integer parteEquipoId;

    @Column(name = "numero_documento", nullable = false, length = 30)
    private String numeroDocumento;
}
