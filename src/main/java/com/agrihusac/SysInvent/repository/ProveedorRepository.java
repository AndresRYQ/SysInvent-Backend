package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.ProveedorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProveedorRepository extends JpaRepository<ProveedorEntity, Integer> {
    List<ProveedorEntity> findAllByActivoTrueOrderByRazonSocialAsc();
    Page<ProveedorEntity> findByRazonSocialContainingIgnoreCase(String filtro, Pageable pageable);
    Page<ProveedorEntity> findByActivoTrueAndRazonSocialContainingIgnoreCase(String filtro, Pageable pageable);
    boolean existsByRucIgnoreCase(String value);
    boolean existsByRucIgnoreCaseAndProveedorIdNot(String value, Integer id);
}
