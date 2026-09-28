package com.agrihusac.SysInvent.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
public class AuditoriaEntity {

    @CreatedDate
    @Column(name = "fecha_registro", updatable = false)
    private LocalDate fechaRegistro;

    @LastModifiedDate
    @Column(name = "fecha_modificacion", insertable = false)
    private LocalDate fechaModificacion;
}
