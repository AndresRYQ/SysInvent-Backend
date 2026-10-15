package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.UnidadMedidaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnidadMedidaRepository extends JpaRepository<UnidadMedidaEntity, Integer> {
    Page<UnidadMedidaEntity> findByActivoTrueAndNombreContainingIgnoreCase(String filtro, Pageable pageable);
    boolean existsByNombreIgnoreCase(String value);
    boolean existsByNombreIgnoreCaseAndUnidadMedidaIdNot(String value, Integer id);
}
