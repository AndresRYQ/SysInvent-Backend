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
            SELECT b.bitacora_id AS bitacoraId,
                   b.fecha_hora AS fechaHora,
                   LTRIM(RTRIM(CONCAT(u.nombres, ' ', u.ape_paterno, ' ', u.ape_materno))) AS nombreUsuario,
                   r.nombre AS nombreRol,
                   b.modulo AS modulo,
                   b.accion AS accion,
                   b.detalle AS detalle,
                   b.registro_id AS registroId
            FROM bitacora b
            LEFT JOIN usuario u ON u.usuario_id = b.usuario_id
            LEFT JOIN rol r ON r.rol_id = b.rol_id
            WHERE (CAST(:busqueda AS VARCHAR(255)) IS NULL OR :busqueda = ''
                   OR LOWER(LTRIM(RTRIM(CONCAT(u.nombres, ' ', u.ape_paterno, ' ', u.ape_materno)))) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                   OR LOWER(COALESCE(b.detalle, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')))
              AND (CAST(:modulo AS VARCHAR(100)) IS NULL OR :modulo = '' OR b.modulo = :modulo)
              AND (CAST(:accion AS VARCHAR(30)) IS NULL OR :accion = '' OR b.accion = :accion)
              AND (CAST(:fechaDesde AS DATE) IS NULL OR b.fecha_hora >= :fechaDesde)
              AND (CAST(:fechaHasta AS DATE) IS NULL OR b.fecha_hora < DATEADD(day, 1, CAST(:fechaHasta AS DATE)))
            ORDER BY b.fecha_hora DESC, b.bitacora_id DESC
            """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM bitacora b
                    LEFT JOIN usuario u ON u.usuario_id = b.usuario_id
                    WHERE (CAST(:busqueda AS VARCHAR(255)) IS NULL OR :busqueda = ''
                           OR LOWER(LTRIM(RTRIM(CONCAT(u.nombres, ' ', u.ape_paterno, ' ', u.ape_materno)))) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                           OR LOWER(COALESCE(b.detalle, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')))
                      AND (CAST(:modulo AS VARCHAR(100)) IS NULL OR :modulo = '' OR b.modulo = :modulo)
                      AND (CAST(:accion AS VARCHAR(30)) IS NULL OR :accion = '' OR b.accion = :accion)
                      AND (CAST(:fechaDesde AS DATE) IS NULL OR b.fecha_hora >= :fechaDesde)
                      AND (CAST(:fechaHasta AS DATE) IS NULL OR b.fecha_hora < DATEADD(day, 1, :fechaHasta))
                    """,
            nativeQuery = true)
    Page<BitacoraProjection> listar(@Param("busqueda") String busqueda,
                                    @Param("modulo") String modulo,
                                    @Param("accion") String accion,
                                    @Param("fechaDesde") LocalDate fechaDesde,
                                    @Param("fechaHasta") LocalDate fechaHasta,
                                    Pageable pageable);
}
