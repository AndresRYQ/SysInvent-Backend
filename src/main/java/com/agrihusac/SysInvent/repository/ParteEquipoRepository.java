package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.ParteEquipoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParteEquipoRepository extends JpaRepository<ParteEquipoEntity, Integer> {
    List<ParteEquipoEntity> findAllByActivoTrueOrderByNombreAsc();
    Page<ParteEquipoEntity> findByNombreContainingIgnoreCase(String filtro, Pageable pageable);
    Page<ParteEquipoEntity> findByActivoTrueAndNombreContainingIgnoreCase(String filtro, Pageable pageable);
    boolean existsByCodigoIgnoreCase(String value);
    boolean existsByCodigoIgnoreCaseAndParteEquipoIdNot(String value, Integer id);
}
