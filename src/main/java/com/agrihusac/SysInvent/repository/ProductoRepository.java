package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.ProductoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<ProductoEntity, Integer> {
    Page<ProductoEntity> findByActivoTrueAndNombreContainingIgnoreCase(String filtro, Pageable pageable);
    boolean existsByCodigoIgnoreCase(String codigo);
    boolean existsByCodigoIgnoreCaseAndProductoIdNot(String codigo, Integer productoId);
}
