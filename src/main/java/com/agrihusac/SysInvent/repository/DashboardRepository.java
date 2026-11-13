package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.projection.DashboardActividadProjection;
import com.agrihusac.SysInvent.model.projection.DashboardResumenProjection;
import com.agrihusac.SysInvent.model.entity.ProductoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Consultas del dashboard.
 *
 * Las fechas de referencia (mes y día actual) se reciben como parámetros para
 * que el SQL sea portable entre SQL Server y MySQL (sin GETDATE(), TOP, etc.).
 */
public interface DashboardRepository extends Repository<ProductoEntity, Integer> {

    @Query(value = """
            SELECT
                (SELECT COUNT(*) FROM producto WHERE activo = 1) AS productosActivos,
                (SELECT COUNT(*) FROM producto
                  WHERE activo = 1 AND stock_actual <= stock_minimo) AS stockCritico,
                (SELECT COUNT(*) FROM producto
                  WHERE activo = 1 AND stock_actual > 0 AND stock_actual <= stock_minimo) AS stockBajo,
                (SELECT COUNT(*) FROM producto
                  WHERE activo = 1 AND stock_actual <= 0) AS productosSinStock,
                (SELECT COUNT(*) FROM ingreso_almacen
                  WHERE estado = 'REGISTRADO'
                    AND fecha_ingreso >= :inicioMes
                    AND fecha_ingreso < :inicioMesSiguiente) AS ingresosMes,
                (SELECT COUNT(*) FROM vale_consumo
                  WHERE estado = 'REGISTRADO'
                    AND fecha_vale >= :inicioMes
                    AND fecha_vale < :inicioMesSiguiente) AS valesMes,
                (SELECT COUNT(*) FROM kardex_movimiento
                  WHERE fecha_movimiento >= :inicioDia
                    AND fecha_movimiento < :inicioDiaSiguiente) AS movimientosHoy
            """, nativeQuery = true)
    DashboardResumenProjection resumen(@Param("inicioMes") LocalDate inicioMes,
                                       @Param("inicioMesSiguiente") LocalDate inicioMesSiguiente,
                                       @Param("inicioDia") LocalDateTime inicioDia,
                                       @Param("inicioDiaSiguiente") LocalDateTime inicioDiaSiguiente);

    /**
     * Últimos movimientos del Kardex. El límite se controla con {@link Pageable}
     * para que Hibernate genere TOP/OFFSET FETCH (SQL Server) o LIMIT (MySQL).
     */
    @Query("""
            select k.fechaMovimiento as fechaMovimiento,
                   k.tipoMovimiento as tipoMovimiento,
                   k.numeroDocumento as documento,
                   p.nombre as producto,
                   k.cantidad as cantidad,
                   k.usuarioResponsable as usuarioResponsable,
                   u.nombres as nombres,
                   u.apePaterno as apePaterno,
                   u.apeMaterno as apeMaterno
              from KardexMovimientoEntity k
              join ProductoEntity p on p.productoId = k.productoId
              left join UsuarioEntity u on u.usuario = k.usuarioResponsable
             order by k.fechaMovimiento desc, k.kardexMovimientoId desc
            """)
    List<DashboardActividadProjection> actividadReciente(Pageable pageable);
}
