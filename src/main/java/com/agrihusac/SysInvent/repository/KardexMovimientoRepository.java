package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.KardexMovimientoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KardexMovimientoRepository extends JpaRepository<KardexMovimientoEntity, Long> {
    List<KardexMovimientoEntity> findByValeConsumoIdOrderByKardexMovimientoIdAsc(Integer valeConsumoId);
}
