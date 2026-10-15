package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.ProveedorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProveedorRepository extends JpaRepository<ProveedorEntity, Integer> {
    Page<ProveedorEntity> findByActivoTrueAndRazonSocialContainingIgnoreCase(String filtro, Pageable pageable);
    boolean existsByRucIgnoreCase(String value);
    boolean existsByRucIgnoreCaseAndProveedorIdNot(String value, Integer id);
}
