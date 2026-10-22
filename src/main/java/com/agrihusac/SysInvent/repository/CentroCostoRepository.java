package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.CentroCostoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CentroCostoRepository extends JpaRepository<CentroCostoEntity, Integer> {
    List<CentroCostoEntity> findAllByActivoTrueOrderByNombreAsc();
    Page<CentroCostoEntity> findByNombreContainingIgnoreCase(String filtro, Pageable pageable);
    Page<CentroCostoEntity> findByActivoTrueAndNombreContainingIgnoreCase(String filtro, Pageable pageable);
    boolean existsByNombreIgnoreCase(String value);
    boolean existsByNombreIgnoreCaseAndCentroCostoIdNot(String value, Integer id);
}
