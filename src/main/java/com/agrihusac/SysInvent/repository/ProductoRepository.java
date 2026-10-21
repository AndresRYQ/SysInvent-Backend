package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.ProductoEntity;
import com.agrihusac.SysInvent.model.projection.ControlAlmacenListadoProjection;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<ProductoEntity, Integer> {

    List<ProductoEntity> findAllByActivoTrueOrderByNombreAsc();

    @Query("""
            select p from ProductoEntity p
            join fetch p.unidadMedida
            where p.activo = true
              and p.tipoProductoId = :tipoProductoId
              and p.proveedorId = :proveedorId
            order by p.nombre asc
            """)
    List<ProductoEntity> findProductosParaCombo(@Param("tipoProductoId") Integer tipoProductoId,
                                                @Param("proveedorId") Integer proveedorId);

    @Query(value = """
            SELECT p.producto_id AS productoId,
                   p.codigo AS codigo,
                   p.nombre AS producto,
                   um.nombre AS unidadMedida,
                   tp.nombre AS tipo,
                   c.nombre AS categoria,
                   pr.razon_social AS proveedor,
                   COALESCE(SUM(CASE WHEN k.tipo_movimiento IN ('ENTRADA_INGRESO', 'AJUSTE_ENTRADA')
                                     THEN k.cantidad ELSE 0 END), 0) AS entradas,
                   COALESCE(SUM(CASE WHEN k.tipo_movimiento IN ('SALIDA_VALE', 'AJUSTE_SALIDA', 'ANULACION_ENTRADA')
                                     THEN k.cantidad ELSE 0 END), 0) AS salidas,
                   p.stock_actual AS stock,
                   CASE WHEN p.stock_actual > 0 THEN 'CON_STOCK' ELSE 'SIN_STOCK' END AS estado
              FROM producto p
              INNER JOIN tipo_producto tp ON tp.tipo_producto_id = p.tipo_producto_id
              INNER JOIN categoria c ON c.categoria_id = p.categoria_id
              INNER JOIN unidad_medida um ON um.unidad_medida_id = p.unidad_medida_id
              INNER JOIN proveedor pr ON pr.proveedor_id = p.proveedor_id
              LEFT JOIN kardex_movimiento k
                ON k.producto_id = p.producto_id
               AND (:fechaDesde IS NULL OR k.fecha_movimiento >= :fechaDesde)
               AND (:fechaHasta IS NULL OR k.fecha_movimiento < :fechaHasta)
             WHERE p.activo = 1
               AND (:busqueda = '' OR LOWER(p.codigo) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                    OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')))
               AND (:tipoProductoId IS NULL OR p.tipo_producto_id = :tipoProductoId)
               AND (:categoriaId IS NULL OR p.categoria_id = :categoriaId)
               AND (:estado IS NULL OR :estado = ''
                    OR (:estado = 'CON_STOCK' AND p.stock_actual > 0)
                    OR (:estado = 'SIN_STOCK' AND p.stock_actual <= 0))
             GROUP BY p.producto_id, p.codigo, p.nombre, um.nombre, tp.nombre, c.nombre,
                      pr.razon_social, p.stock_actual
             ORDER BY p.nombre ASC
            """,
            countQuery = """
            SELECT COUNT(*)
              FROM producto p
             WHERE p.activo = 1
               AND (:busqueda = '' OR LOWER(p.codigo) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                    OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')))
               AND (:tipoProductoId IS NULL OR p.tipo_producto_id = :tipoProductoId)
               AND (:categoriaId IS NULL OR p.categoria_id = :categoriaId)
               AND (:estado IS NULL OR :estado = ''
                    OR (:estado = 'CON_STOCK' AND p.stock_actual > 0)
                    OR (:estado = 'SIN_STOCK' AND p.stock_actual <= 0))
            """,
            nativeQuery = true)
    Page<ControlAlmacenListadoProjection> listarControlAlmacen(
            @Param("busqueda") String busqueda,
            @Param("tipoProductoId") Integer tipoProductoId,
            @Param("categoriaId") Integer categoriaId,
            @Param("estado") String estado,
            @Param("fechaDesde") LocalDateTime fechaDesde,
            @Param("fechaHasta") LocalDateTime fechaHasta,
            Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from ProductoEntity p where p.productoId = :productoId")
    Optional<ProductoEntity> buscarParaActualizar(@Param("productoId") Integer productoId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update ProductoEntity p
               set p.stockActual = p.stockActual - :cantidad
             where p.productoId = :productoId
               and p.activo = true
               and p.stockActual >= :cantidad
            """)
    int descontarStockDisponible(@Param("productoId") Integer productoId,
                                 @Param("cantidad") BigDecimal cantidad);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update ProductoEntity p
               set p.stockActual = p.stockActual + :cantidad
             where p.productoId = :productoId
            """)
    int incrementarStock(@Param("productoId") Integer productoId,
                         @Param("cantidad") BigDecimal cantidad);

    Page<ProductoEntity> findByNombreContainingIgnoreCase(String filtro, Pageable pageable);

    Page<ProductoEntity> findByActivoTrueAndNombreContainingIgnoreCase(String filtro, Pageable pageable);

    boolean existsByCodigoIgnoreCase(String codigo);

    boolean existsByCodigoIgnoreCaseAndProductoIdNot(String codigo, Integer productoId);
}
