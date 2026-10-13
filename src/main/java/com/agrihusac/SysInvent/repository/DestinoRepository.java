package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.DestinoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DestinoRepository extends JpaRepository<DestinoEntity, Integer> {
    Page<DestinoEntity> findByActivoTrueAndNombreContainingIgnoreCase(String filtro, Pageable pageable);
    boolean existsByNombreIgnoreCase(String value);
    boolean existsByNombreIgnoreCaseAndDestinoIdNot(String value, Integer id);
}
