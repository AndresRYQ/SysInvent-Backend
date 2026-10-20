package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.IngresoAlmacenEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IngresoAlmacenRepository extends JpaRepository<IngresoAlmacenEntity, Integer> {

    boolean existsByProveedor_ProveedorIdAndTipoDocumento_TipoDocumentoIdAndNumeroDocumentoIgnoreCase(
            Integer proveedorId, Integer tipoDocumentoId, String numeroDocumento);

    @Query("SELECT COALESCE(MAX(i.ingresoAlmacenId), 0) FROM IngresoAlmacenEntity i")
    long obtenerUltimoCorrelativo();

    Page<IngresoAlmacenEntity> findByNumeroIngresoContainingIgnoreCase(String numeroIngreso, Pageable pageable);
}
