package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.ContactoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactoRepository extends JpaRepository<ContactoEntity, Integer> {
    Page<ContactoEntity> findByActivoTrueAndNombreCompletoContainingIgnoreCase(String filtro, Pageable pageable);
}
