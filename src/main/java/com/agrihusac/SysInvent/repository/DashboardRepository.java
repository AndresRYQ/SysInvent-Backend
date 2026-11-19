package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.projection.DashboardActividadProjection;
import com.agrihusac.SysInvent.model.projection.DashboardResumenProjection;
import com.agrihusac.SysInvent.model.entity.ProductoEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface DashboardRepository extends Repository<ProductoEntity, Integer> {

    @Query(value = """
        SELECT
            (SELECT COUNT(*)
               FROM producto
              WHERE activo = CAST(1 AS BIT)) AS productosActivos,

            (SELECT COUNT(*)
               FROM producto
              WHERE activo = CAST(1 AS BIT)
                AND stock_actual > 0
                AND stock_actual <= stock_minimo) AS stockCritico,

            (SELECT COUNT(*)
               FROM producto
              WHERE activo = CAST(1 AS BIT)
                AND stock_actual <= 0) AS productosSinStock,

            (SELECT COUNT(*)
               FROM ingreso_almacen
              WHERE estado = 'REGISTRADO'
                AND fecha_ingreso >= :inicioMes
                AND fecha_ingreso < :siguienteMes) AS ingresosMes,

            (SELECT COUNT(*)
               FROM vale_consumo
              WHERE estado = 'REGISTRADO'
                AND fecha_vale >= :inicioMes
                AND fecha_vale < :siguienteMes) AS valesMes,

            (SELECT COUNT(*)
               FROM kardex_movimiento
              WHERE fecha_movimiento >= :inicioDia
                AND fecha_movimiento < :siguienteDia) AS movimientosHoy
        """, nativeQuery = true)
    DashboardResumenProjection resumen(
            @Param("inicioMes") LocalDate inicioMes,
            @Param("siguienteMes") LocalDate siguienteMes,
            @Param("inicioDia") LocalDateTime inicioDia,
            @Param("siguienteDia") LocalDateTime siguienteDia
    );

    @Query(value = """
        SELECT k.fecha_movimiento AS fecha,
               CASE
                   WHEN k.tipo_movimiento IN (
                       'ENTRADA_INGRESO',
                       'AJUSTE_ENTRADA',
                       'ANULACION_ENTRADA'
                   )
                   THEN 'Ingreso'
                   ELSE 'Vale'
               END AS tipo,
               k.numero_documento AS documento,
               p.nombre AS producto,
               k.cantidad AS cantidad,
               COALESCE(
                   NULLIF(
                       LTRIM(RTRIM(CONCAT(
                           u.nombres,
                           ' ',
                           u.ape_paterno,
                           ' ',
                           u.ape_materno
                       ))),
                       ''
                   ),
                   k.usuario_responsable
               ) AS responsable
          FROM kardex_movimiento k
          INNER JOIN producto p
                  ON p.producto_id = k.producto_id
          LEFT JOIN usuario u
                 ON u.usuario = k.usuario_responsable
         ORDER BY k.fecha_movimiento DESC,
                  k.kardex_movimiento_id DESC
        """, nativeQuery = true)
    List<DashboardActividadProjection> actividadReciente(Pageable pageable);
}
