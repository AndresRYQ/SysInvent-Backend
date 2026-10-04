package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.CategoriaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<CategoriaEntity, Integer> {

    Page<CategoriaEntity> findByActivoTrueAndNombreContainingIgnoreCase(String nombre, Pageable pageable);
}
