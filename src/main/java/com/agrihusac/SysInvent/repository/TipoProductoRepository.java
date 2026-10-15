package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.TipoProductoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoProductoRepository extends JpaRepository<TipoProductoEntity, Integer> {
    Page<TipoProductoEntity> findByActivoTrueAndNombreContainingIgnoreCase(String filtro, Pageable pageable);
    boolean existsByNombreIgnoreCase(String value);
    boolean existsByNombreIgnoreCaseAndTipoProductoIdNot(String value, Integer id);
}
