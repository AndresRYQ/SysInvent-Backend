package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.TipoProductoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoProductoRepository extends JpaRepository<TipoProductoEntity, Integer> {
    List<TipoProductoEntity> findAllByActivoTrueOrderByNombreAsc();
    Page<TipoProductoEntity> findByNombreContainingIgnoreCase(String filtro, Pageable pageable);
    Page<TipoProductoEntity> findByActivoTrueAndNombreContainingIgnoreCase(String filtro, Pageable pageable);
    boolean existsByNombreIgnoreCase(String value);
    boolean existsByNombreIgnoreCaseAndTipoProductoIdNot(String value, Integer id);
}
