package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.RolEntity;
import com.agrihusac.SysInvent.model.projection.RolProjection;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;

public interface RolRepository extends JpaRepository<RolEntity, Integer> {

    List<RolEntity> findAllByRolIdInAndActivoTrue(Collection<Integer> rolesIds);

    @Query(value = """
            SELECT r.rolId AS rolId,
                   r.nombre AS nombre,
                   r.descripcion AS descripcion
            FROM RolEntity r
            WHERE r.activo = true
              AND (:nombre IS NULL OR :nombre = '' OR LOWER(r.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
            """,
            countQuery = """
                SELECT COUNT(r)
                FROM RolEntity r
                WHERE r.activo = true
                  AND (:nombre IS NULL OR :nombre = '' OR LOWER(r.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
            """,
            nativeQuery = false)
    Page<RolProjection> listarRoles(@Param("nombre") String nombre,
                                    @PageableDefault(page = 0, size = 10) Pageable pageable);
}
