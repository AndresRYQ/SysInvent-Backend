package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.IngresoAlmacenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngresoAlmacenRepository extends JpaRepository<IngresoAlmacenEntity, Integer> {
}