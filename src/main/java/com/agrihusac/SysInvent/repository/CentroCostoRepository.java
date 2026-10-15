package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.CentroCostoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CentroCostoRepository extends JpaRepository<CentroCostoEntity, Integer> {
    Page<CentroCostoEntity> findByActivoTrueAndNombreContainingIgnoreCase(String filtro, Pageable pageable);
    boolean existsByNombreIgnoreCase(String value);
    boolean existsByNombreIgnoreCaseAndCentroCostoIdNot(String value, Integer id);
}
