package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.BitacoraEntity;
import com.agrihusac.SysInvent.model.projection.BitacoraProjection;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BitacoraRepository extends JpaRepository<BitacoraEntity, Long> {

    @Query(value = """
            SELECT CONCAT(CONCAT(RIGHT(CONCAT('0', DAY(b.fecha_hora)), 2), '/', RIGHT(CONCAT('0', MONTH(b.fecha_hora)), 2), '/', YEAR(b.fecha_hora)),
                          ' ', LEFT(CAST(CAST(b.fecha_hora AS TIME) AS CHAR(20)), 8)) AS fechaHora,
                   LTRIM(RTRIM(CONCAT(u.nombres, ' ', u.ape_paterno, ' ', u.ape_materno))) AS nombreUsuario,
                   r.nombre AS nombreRol,
                   b.modulo AS modulo,
                   b.accion AS accion,
                   b.detalle AS detalle
            FROM bitacora b
            LEFT JOIN usuario u ON u.usuario_id = b.usuario_id
            LEFT JOIN rol r ON r.rol_id = b.rol_id
            WHERE (:busqueda IS NULL OR :busqueda = ''
                   OR LOWER(LTRIM(RTRIM(CONCAT(u.nombres, ' ', u.ape_paterno, ' ', u.ape_materno)))) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                   OR LOWER(COALESCE(b.detalle, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')))
              AND (:modulo IS NULL OR :modulo = '' OR b.modulo = :modulo)
              AND (:accion IS NULL OR :accion = '' OR b.accion = :accion)
              AND (:fechaDesde IS NULL OR b.fecha_hora >= :fechaDesde)
              AND (:fechaHastaExclusiva IS NULL OR b.fecha_hora < :fechaHastaExclusiva)
            ORDER BY b.fecha_hora DESC, b.bitacora_id DESC
            """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM bitacora b
                    LEFT JOIN usuario u ON u.usuario_id = b.usuario_id
                    WHERE (:busqueda IS NULL OR :busqueda = ''
                           OR LOWER(LTRIM(RTRIM(CONCAT(u.nombres, ' ', u.ape_paterno, ' ', u.ape_materno)))) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                           OR LOWER(COALESCE(b.detalle, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')))
                      AND (:modulo IS NULL OR :modulo = '' OR b.modulo = :modulo)
                      AND (:accion IS NULL OR :accion = '' OR b.accion = :accion)
                      AND (:fechaDesde IS NULL OR b.fecha_hora >= :fechaDesde)
                      AND (:fechaHastaExclusiva IS NULL OR b.fecha_hora < :fechaHastaExclusiva)
                    """,
            nativeQuery = true)
    Page<BitacoraProjection> listar(@Param("busqueda") String busqueda,
                                    @Param("modulo") String modulo,
                                    @Param("accion") String accion,
                                    @Param("fechaDesde") LocalDate fechaDesde,
                                    @Param("fechaHastaExclusiva") LocalDate fechaHastaExclusiva,
                                    Pageable pageable);
}
