package com.agrihusac.SysInvent.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bitacora")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BitacoraEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bitacora_id")
    private Long bitacoraId;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "usuario_id")
    private Integer usuarioId;

    @Column(name = "rol_id")
    private Integer rolId;

    @Column(name = "modulo", nullable = false, length = 100)
    private String modulo;

    @Column(name = "accion", nullable = false, length = 30)
    private String accion;

    @Column(name = "detalle", length = 1000)
    private String detalle;

    @Column(name = "registro_id", length = 50)
    private String registroId;
}
