package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.ValeConsumoEntity;
import com.agrihusac.SysInvent.model.projection.ValeConsumoListadoProjection;
import com.agrihusac.SysInvent.model.projection.ReporteValeListadoProjection;
import com.agrihusac.SysInvent.model.projection.ReporteProductoMasPedidoProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface ValeConsumoRepository extends JpaRepository<ValeConsumoEntity, Integer> {

    @Query(value = """
            SELECT CAST(ROW_NUMBER() OVER (ORDER BY SUM(dv.cantidad) DESC, p.nombre ASC) AS INT) AS posicion,
                   p.producto_id AS productoId,
                   p.codigo AS codigoProducto,
                   p.nombre AS producto,
                   tp.nombre AS tipoProducto,
                   cat.nombre AS categoria,
                   SUM(dv.cantidad) AS cantidad,
                   um.nombre AS unidadMedida,
                   COUNT(DISTINCT v.vale_consumo_id) AS vales,
                   COUNT(dv.distribucion_vale_consumo_id) AS distribuciones,
                   COUNT(DISTINCT dv.destino_id) AS destinos,
                   CAST(SUM(dv.cantidad) * 100.0 /
                        NULLIF(SUM(SUM(dv.cantidad)) OVER (), 0) AS DECIMAL(10,2)) AS participacion,
                   SUM(dv.cantidad * det.precio_unitario) AS valorizado
              FROM vale_consumo v
              INNER JOIN detalle_vale_consumo det ON det.vale_consumo_id = v.vale_consumo_id
              INNER JOIN distribucion_vale_consumo dv
                      ON dv.detalle_vale_consumo_id = det.detalle_vale_consumo_id
              INNER JOIN producto p ON p.producto_id = det.producto_id
              INNER JOIN tipo_producto tp ON tp.tipo_producto_id = p.tipo_producto_id
              INNER JOIN categoria cat ON cat.categoria_id = p.categoria_id
              INNER JOIN unidad_medida um ON um.unidad_medida_id = p.unidad_medida_id
             WHERE (:busqueda = '' OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                    OR LOWER(p.codigo) LIKE LOWER(CONCAT('%', :busqueda, '%')))
               AND (:tipoProductoId IS NULL OR p.tipo_producto_id = :tipoProductoId)
               AND (:categoriaId IS NULL OR p.categoria_id = :categoriaId)
               AND (:destinoId IS NULL OR dv.destino_id = :destinoId)
               AND (:fechaDesde IS NULL OR v.fecha_vale >= :fechaDesde)
               AND (:fechaHasta IS NULL OR v.fecha_vale <= :fechaHasta)
             GROUP BY p.producto_id, p.codigo, p.nombre, tp.nombre, cat.nombre, um.nombre
             ORDER BY cantidad DESC, p.nombre ASC
            """,
            countQuery = """
            SELECT COUNT(DISTINCT p.producto_id)
              FROM vale_consumo v
              INNER JOIN detalle_vale_consumo det ON det.vale_consumo_id = v.vale_consumo_id
              INNER JOIN distribucion_vale_consumo dv
                      ON dv.detalle_vale_consumo_id = det.detalle_vale_consumo_id
              INNER JOIN producto p ON p.producto_id = det.producto_id
             WHERE (:busqueda = '' OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                    OR LOWER(p.codigo) LIKE LOWER(CONCAT('%', :busqueda, '%')))
               AND (:tipoProductoId IS NULL OR p.tipo_producto_id = :tipoProductoId)
               AND (:categoriaId IS NULL OR p.categoria_id = :categoriaId)
               AND (:destinoId IS NULL OR dv.destino_id = :destinoId)
               AND (:fechaDesde IS NULL OR v.fecha_vale >= :fechaDesde)
               AND (:fechaHasta IS NULL OR v.fecha_vale <= :fechaHasta)
            """,
            nativeQuery = true)
    Page<ReporteProductoMasPedidoProjection> listarProductosMasPedidos(
            @Param("busqueda") String busqueda,
            @Param("tipoProductoId") Integer tipoProductoId,
            @Param("categoriaId") Integer categoriaId,
            @Param("destinoId") Integer destinoId,
            @Param("fechaDesde") java.time.LocalDate fechaDesde,
            @Param("fechaHasta") java.time.LocalDate fechaHasta,
            Pageable pageable);

    @Query(value = """
            SELECT v.vale_consumo_id AS valeConsumoId,
                   v.numero_vale AS numeroVale,
                   CONVERT(VARCHAR(10), v.fecha_vale, 103) AS fechaVale,
                   cc.nombre AS centroCosto,
                   v.solicitante AS solicitante,
                   p.codigo AS codigoProducto,
                   p.nombre AS producto,
                   d.nombre AS destino,
                   pe.nombre AS parteEquipo,
                   dv.cantidad AS cantidad,
                   um.nombre AS unidadMedida,
                   det.precio_unitario AS precio,
                   dv.cantidad * det.precio_unitario AS subtotal,
                   v.estado AS estado
              FROM vale_consumo v
              INNER JOIN centro_costo cc ON cc.centro_costo_id = v.centro_costo_id
              INNER JOIN detalle_vale_consumo det ON det.vale_consumo_id = v.vale_consumo_id
              INNER JOIN distribucion_vale_consumo dv
                      ON dv.detalle_vale_consumo_id = det.detalle_vale_consumo_id
              INNER JOIN producto p ON p.producto_id = det.producto_id
              INNER JOIN unidad_medida um ON um.unidad_medida_id = p.unidad_medida_id
              INNER JOIN destino d ON d.destino_id = dv.destino_id
              LEFT JOIN parte_equipo pe ON pe.parte_equipo_id = dv.parte_equipo_id
             WHERE (:busqueda = '' OR LOWER(v.numero_vale) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                    OR LOWER(v.solicitante) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                    OR LOWER(cc.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                    OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                    OR LOWER(d.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                    OR LOWER(pe.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')))
               AND (:centroCostoId IS NULL OR v.centro_costo_id = :centroCostoId)
               AND (:destinoId IS NULL OR dv.destino_id = :destinoId)
               AND (:parteEquipoId IS NULL OR dv.parte_equipo_id = :parteEquipoId)
               AND (:tipoProductoId IS NULL OR p.tipo_producto_id = :tipoProductoId)
               AND (:productoId IS NULL OR p.producto_id = :productoId)
               AND (:estado = '' OR :estado IS NULL OR v.estado = :estado)
               AND (:fechaDesde IS NULL OR v.fecha_vale >= :fechaDesde)
               AND (:fechaHasta IS NULL OR v.fecha_vale <= :fechaHasta)
             ORDER BY v.fecha_vale DESC, v.vale_consumo_id DESC,
                      dv.distribucion_vale_consumo_id ASC
            """,
            countQuery = """
            SELECT COUNT(*)
              FROM vale_consumo v
              INNER JOIN centro_costo cc ON cc.centro_costo_id = v.centro_costo_id
              INNER JOIN detalle_vale_consumo det ON det.vale_consumo_id = v.vale_consumo_id
              INNER JOIN distribucion_vale_consumo dv
                      ON dv.detalle_vale_consumo_id = det.detalle_vale_consumo_id
              INNER JOIN producto p ON p.producto_id = det.producto_id
              INNER JOIN destino d ON d.destino_id = dv.destino_id
              LEFT JOIN parte_equipo pe ON pe.parte_equipo_id = dv.parte_equipo_id
             WHERE (:busqueda = '' OR LOWER(v.numero_vale) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                    OR LOWER(v.solicitante) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                    OR LOWER(cc.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                    OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                    OR LOWER(d.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                    OR LOWER(pe.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')))
               AND (:centroCostoId IS NULL OR v.centro_costo_id = :centroCostoId)
               AND (:destinoId IS NULL OR dv.destino_id = :destinoId)
               AND (:parteEquipoId IS NULL OR dv.parte_equipo_id = :parteEquipoId)
               AND (:tipoProductoId IS NULL OR p.tipo_producto_id = :tipoProductoId)
               AND (:productoId IS NULL OR p.producto_id = :productoId)
               AND (:estado = '' OR :estado IS NULL OR v.estado = :estado)
               AND (:fechaDesde IS NULL OR v.fecha_vale >= :fechaDesde)
               AND (:fechaHasta IS NULL OR v.fecha_vale <= :fechaHasta)
            """,
            nativeQuery = true)
    Page<ReporteValeListadoProjection> listarReporte(
            @Param("busqueda") String busqueda,
            @Param("centroCostoId") Integer centroCostoId,
            @Param("destinoId") Integer destinoId,
            @Param("parteEquipoId") Integer parteEquipoId,
            @Param("tipoProductoId") Integer tipoProductoId,
            @Param("productoId") Integer productoId,
            @Param("estado") String estado,
            @Param("fechaDesde") java.time.LocalDate fechaDesde,
            @Param("fechaHasta") java.time.LocalDate fechaHasta,
            Pageable pageable);

    @Query(value = """
            SELECT v.vale_consumo_id AS id,
                   v.numero_vale AS numeroVale,
                   v.fecha_vale AS fechaVale,
                   cc.nombre AS centroCosto,
                   v.solicitante AS solicitante,
                   (SELECT COUNT(DISTINCT d.producto_id)
                      FROM detalle_vale_consumo d
                     WHERE d.vale_consumo_id = v.vale_consumo_id) AS productos,
                   (SELECT COUNT(DISTINCT dv.destino_id)
                      FROM distribucion_vale_consumo dv
                      INNER JOIN detalle_vale_consumo d2
                              ON d2.detalle_vale_consumo_id = dv.detalle_vale_consumo_id
                     WHERE d2.vale_consumo_id = v.vale_consumo_id) AS destinos,
                   COALESCE((SELECT SUM(dv2.cantidad)
                               FROM distribucion_vale_consumo dv2
                               INNER JOIN detalle_vale_consumo d3
                                       ON d3.detalle_vale_consumo_id = dv2.detalle_vale_consumo_id
                              WHERE d3.vale_consumo_id = v.vale_consumo_id), 0) AS cantidadTotal,
                   COALESCE((SELECT SUM(dv3.cantidad * d4.precio_unitario)
                               FROM distribucion_vale_consumo dv3
                               INNER JOIN detalle_vale_consumo d4
                                       ON d4.detalle_vale_consumo_id = dv3.detalle_vale_consumo_id
                              WHERE d4.vale_consumo_id = v.vale_consumo_id), 0) AS total,
                   v.estado AS estado
              FROM vale_consumo v
              INNER JOIN centro_costo cc ON cc.centro_costo_id = v.centro_costo_id
             WHERE (:busqueda = '' OR LOWER(v.numero_vale) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                    OR LOWER(v.solicitante) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                    OR LOWER(v.motivo) LIKE LOWER(CONCAT('%', :busqueda, '%')))
               AND (:centroCostoId IS NULL OR v.centro_costo_id = :centroCostoId)
               AND (:estado = '' OR :estado IS NULL OR v.estado = :estado)
               AND (:fechaDesde IS NULL OR v.fecha_vale >= :fechaDesde)
               AND (:fechaHasta IS NULL OR v.fecha_vale <= :fechaHasta)
             ORDER BY v.fecha_vale DESC, v.vale_consumo_id DESC
            """,
            countQuery = """
            SELECT COUNT(*)
              FROM vale_consumo v
             WHERE (:busqueda = '' OR LOWER(v.numero_vale) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                    OR LOWER(v.solicitante) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                    OR LOWER(v.motivo) LIKE LOWER(CONCAT('%', :busqueda, '%')))
               AND (:centroCostoId IS NULL OR v.centro_costo_id = :centroCostoId)
               AND (:estado = '' OR :estado IS NULL OR v.estado = :estado)
               AND (:fechaDesde IS NULL OR v.fecha_vale >= :fechaDesde)
               AND (:fechaHasta IS NULL OR v.fecha_vale <= :fechaHasta)
            """,
            nativeQuery = true)
    Page<ValeConsumoListadoProjection> listarResumen(
            @Param("busqueda") String busqueda,
            @Param("centroCostoId") Integer centroCostoId,
            @Param("estado") String estado,
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta,
            Pageable pageable);
}
