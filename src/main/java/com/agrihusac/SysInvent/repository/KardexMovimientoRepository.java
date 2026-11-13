package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.KardexMovimientoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface KardexMovimientoRepository extends JpaRepository<KardexMovimientoEntity, Long> {

        /**
         * Movimientos para el reporte de Kardex. La lista de tipos siempre se
         * envía (todos los tipos cuando no se filtra) para mantener el JPQL portable.
         */
        @Query("""
                        select movimiento from KardexMovimientoEntity movimiento
                         where (:productoId is null or movimiento.productoId = :productoId)
                           and movimiento.tipoMovimiento in :tipos
                           and (:desde is null or movimiento.fechaMovimiento >= :desde)
                           and (:hasta is null or movimiento.fechaMovimiento < :hasta)
                         order by movimiento.fechaMovimiento desc, movimiento.kardexMovimientoId desc
                        """)
        List<KardexMovimientoEntity> buscarParaReporte(
                        @Param("productoId") Integer productoId,
                        @Param("tipos") java.util.Collection<String> tipos,
                        @Param("desde") LocalDateTime desde,
                        @Param("hasta") LocalDateTime hasta);

    List<KardexMovimientoEntity> findByValeConsumoIdOrderByKardexMovimientoIdAsc(Integer valeConsumoId);
    Page<KardexMovimientoEntity> findByProductoIdOrderByFechaMovimientoAscKardexMovimientoIdAsc(
            Integer productoId, Pageable pageable);

        @Query("""
                        select movimiento from KardexMovimientoEntity movimiento
                         where (:productoId is null or movimiento.productoId = :productoId)
                             and (:tipoMovimiento is null or movimiento.tipoMovimiento = :tipoMovimiento)
                             and (:desde is null or movimiento.fechaMovimiento >= :desde)
                               and (:hasta is null or movimiento.fechaMovimiento < :hasta)
                         order by movimiento.fechaMovimiento desc, movimiento.kardexMovimientoId desc
                        """)
        List<KardexMovimientoEntity> buscarMovimientos(
                        @Param("productoId") Integer productoId,
                        @Param("tipoMovimiento") String tipoMovimiento,
                        @Param("desde") LocalDateTime desde,
                        @Param("hasta") LocalDateTime hasta
        );
}
