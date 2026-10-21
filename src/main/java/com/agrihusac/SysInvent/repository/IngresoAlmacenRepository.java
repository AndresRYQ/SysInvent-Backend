package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.IngresoAlmacenEntity;
import com.agrihusac.SysInvent.model.projection.IngresoAlmacenListadoProjection;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IngresoAlmacenRepository extends JpaRepository<IngresoAlmacenEntity, Integer> {
    boolean existsByProveedor_ProveedorIdAndTipoDocumento_TipoDocumentoIdAndNumeroDocumentoIgnoreCase(
            Integer proveedorId, Integer tipoDocumentoId, String numeroDocumento);

    @Query("SELECT COALESCE(MAX(i.ingresoAlmacenId), 0) FROM IngresoAlmacenEntity i")
    long obtenerUltimoCorrelativo();

    @Query(value = """
            SELECT i.ingreso_almacen_id AS ingresoAlmacenId,
                   i.numero_ingreso AS numeroIngreso,
                   CONVERT(VARCHAR(10), i.fecha_ingreso, 103) AS fechaIngreso,
                   p.razon_social AS proveedor,
                   c.nombre_completo AS contacto,
                   CONCAT(td.nombre, ' - ', i.numero_documento) AS documento,
                   COUNT(DISTINCT d.producto_id) AS productos,
                   STRING_AGG(CONCAT(pr.nombre, ': ', FORMAT(d.cantidad, '0.####', 'en-US'), ' ', um.nombre), ', ') AS cantidadIngresada,
                   SUM(d.cantidad * d.precio_unitario) AS total,
                   i.estado AS estado
            FROM ingreso_almacen i
            INNER JOIN proveedor p ON p.proveedor_id = i.proveedor_id
            INNER JOIN contacto c ON c.contacto_id = i.contacto_id
            INNER JOIN tipo_documento td ON td.tipo_documento_id = i.tipo_documento_id
            INNER JOIN detalle_ingreso_almacen d ON d.ingreso_almacen_id = i.ingreso_almacen_id
            INNER JOIN producto pr ON pr.producto_id = d.producto_id
            INNER JOIN unidad_medida um ON um.unidad_medida_id = pr.unidad_medida_id
            WHERE (:busqueda IS NULL OR :busqueda = ''
                   OR LOWER(i.numero_ingreso) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                   OR LOWER(i.numero_documento) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                   OR LOWER(p.razon_social) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                   OR LOWER(c.nombre_completo) LIKE LOWER(CONCAT('%', :busqueda, '%')))
              AND (:proveedorId IS NULL OR i.proveedor_id = :proveedorId)
              AND (:estado IS NULL OR :estado = '' OR i.estado = :estado)
              AND (:fechaDesde IS NULL OR i.fecha_ingreso >= :fechaDesde)
              AND (:fechaHasta IS NULL OR i.fecha_ingreso <= :fechaHasta)
            GROUP BY i.ingreso_almacen_id, i.numero_ingreso, i.fecha_ingreso,
                     p.razon_social, c.nombre_completo, td.nombre,
                     i.numero_documento, i.estado
            ORDER BY i.fecha_ingreso DESC, i.ingreso_almacen_id DESC
            """,
            countQuery = """
            SELECT COUNT(*)
            FROM ingreso_almacen i
            INNER JOIN proveedor p ON p.proveedor_id = i.proveedor_id
            INNER JOIN contacto c ON c.contacto_id = i.contacto_id
            WHERE (:busqueda IS NULL OR :busqueda = ''
                   OR LOWER(i.numero_ingreso) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                   OR LOWER(i.numero_documento) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                   OR LOWER(p.razon_social) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                   OR LOWER(c.nombre_completo) LIKE LOWER(CONCAT('%', :busqueda, '%')))
              AND (:proveedorId IS NULL OR i.proveedor_id = :proveedorId)
              AND (:estado IS NULL OR :estado = '' OR i.estado = :estado)
              AND (:fechaDesde IS NULL OR i.fecha_ingreso >= :fechaDesde)
              AND (:fechaHasta IS NULL OR i.fecha_ingreso <= :fechaHasta)
            """,
            nativeQuery = true)
    Page<IngresoAlmacenListadoProjection> listarResumen(
            @Param("busqueda") String busqueda,
            @Param("proveedorId") Integer proveedorId,
            @Param("estado") String estado,
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta,
            Pageable pageable);
}
