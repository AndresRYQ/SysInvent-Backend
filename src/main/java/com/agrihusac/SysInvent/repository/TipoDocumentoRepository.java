package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.TipoDocumentoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoDocumentoRepository extends JpaRepository<TipoDocumentoEntity, Integer> {
    Page<TipoDocumentoEntity> findByActivoTrueAndNombreContainingIgnoreCase(String filtro, Pageable pageable);
    boolean existsByNombreIgnoreCase(String value);
    boolean existsByNombreIgnoreCaseAndTipoDocumentoIdNot(String value, Integer id);
}
