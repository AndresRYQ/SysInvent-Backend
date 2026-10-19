package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.CategoriaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<CategoriaEntity, Integer> {
    List<CategoriaEntity> findAllByActivoTrueOrderByNombreAsc();

    Page<CategoriaEntity> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
    Page<CategoriaEntity> findByActivoTrueAndNombreContainingIgnoreCase(String nombre, Pageable pageable);

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCaseAndCategoriaIdNot(String nombre, Integer categoriaId);
}
