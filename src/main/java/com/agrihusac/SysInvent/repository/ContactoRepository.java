package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.ContactoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactoRepository extends JpaRepository<ContactoEntity, Integer> {
    Page<ContactoEntity> findByNombreCompletoContainingIgnoreCase(String filtro, Pageable pageable);
    Page<ContactoEntity> findByActivoTrueAndNombreCompletoContainingIgnoreCase(String filtro, Pageable pageable);
    Page<ContactoEntity> findByProveedorProveedorIdAndActivoTrueAndNombreCompletoContainingIgnoreCase(Integer proveedorId, String filtro, Pageable pageable);
    List<ContactoEntity> findByProveedorProveedorIdAndActivoTrueOrderByNombreCompletoAsc(Integer proveedorId);
}
