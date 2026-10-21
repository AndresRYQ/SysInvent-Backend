package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.projection.DashboardActividadProjection;
import com.agrihusac.SysInvent.model.projection.DashboardResumenProjection;
import com.agrihusac.SysInvent.model.entity.ProductoEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface DashboardRepository extends Repository<ProductoEntity, Integer> {

    @Query(value = """
            SELECT
                (SELECT COUNT(*) FROM producto WHERE activo = 1) AS productosActivos,
                (SELECT COUNT(*) FROM producto WHERE activo = 1 AND stock_actual <= stock_minimo) AS stockCritico,
                (SELECT COUNT(*) FROM producto WHERE activo = 1 AND stock_actual = 0) AS productosSinStock,
                (SELECT COUNT(*) FROM ingreso_almacen
                  WHERE estado = 'REGISTRADO'
                    AND YEAR(fecha_ingreso) = YEAR(GETDATE())
                    AND MONTH(fecha_ingreso) = MONTH(GETDATE())) AS ingresosMes,
                (SELECT COUNT(*) FROM vale_consumo
                  WHERE estado = 'REGISTRADO'
                    AND YEAR(fecha_vale) = YEAR(GETDATE())
                    AND MONTH(fecha_vale) = MONTH(GETDATE())) AS valesMes,
                (SELECT COUNT(*) FROM kardex_movimiento
                  WHERE CAST(fecha_movimiento AS DATE) = CAST(GETDATE() AS DATE)) AS movimientosHoy
            """, nativeQuery = true)
    DashboardResumenProjection resumen();

    @Query(value = """
            SELECT TOP 5
                   CONVERT(VARCHAR(10), k.fecha_movimiento, 103) AS fecha,
                   CASE WHEN k.tipo_movimiento IN ('ENTRADA_INGRESO', 'AJUSTE_ENTRADA')
                        THEN 'Ingreso' ELSE 'Vale' END AS tipo,
                   k.numero_documento AS documento,
                   p.nombre AS producto,
                   k.cantidad AS cantidad,
                   COALESCE(NULLIF(CONCAT(u.nombres, ' ', u.ape_paterno, ' ', u.ape_materno), '  '),
                            k.usuario_responsable) AS responsable
              FROM kardex_movimiento k
              INNER JOIN producto p ON p.producto_id = k.producto_id
              LEFT JOIN usuario u ON u.usuario = k.usuario_responsable
             ORDER BY k.fecha_movimiento DESC, k.kardex_movimiento_id DESC
            """, nativeQuery = true)
    List<DashboardActividadProjection> actividadReciente();
}
