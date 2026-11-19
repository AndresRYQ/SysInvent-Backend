package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.ContactoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ContactoRepository extends JpaRepository<ContactoEntity, Integer> {
    Page<ContactoEntity> findByNombreCompletoContainingIgnoreCase(String filtro, Pageable pageable);

    Page<ContactoEntity> findByActivoTrueAndNombreCompletoContainingIgnoreCase(String filtro, Pageable pageable);

    Page<ContactoEntity> findByProveedorProveedorIdAndActivoTrueAndNombreCompletoContainingIgnoreCase(Integer proveedorId, String filtro, Pageable pageable);

    List<ContactoEntity> findByProveedorProveedorIdAndActivoTrueOrderByNombreCompletoAsc(Integer proveedorId);

    @Query("""
        select c
        from ContactoEntity c
        join c.proveedor p
        where (
            :filtro = ''
            or lower(c.nombreCompleto) like lower(concat('%', :filtro, '%'))
            or lower(coalesce(c.cargo, '')) like lower(concat('%', :filtro, '%'))
            or lower(coalesce(c.telefono, '')) like lower(concat('%', :filtro, '%'))
            or lower(coalesce(c.correo, '')) like lower(concat('%', :filtro, '%'))
            or lower(p.razonSocial) like lower(concat('%', :filtro, '%'))
        )
        and (:proveedorId is null or p.proveedorId = :proveedorId)
        and (:estado is null or c.activo = :estado)
        """)
    Page<ContactoEntity> buscarContactos(
            @Param("filtro") String filtro,
            @Param("proveedorId") Integer proveedorId,
            @Param("estado") Boolean estado,
            Pageable pageable
    );
}
