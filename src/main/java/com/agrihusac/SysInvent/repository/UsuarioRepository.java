package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.UsuarioEntity;
import com.agrihusac.SysInvent.model.projection.UsuarioProjection;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository
        extends JpaRepository<UsuarioEntity, Integer> {

    @Query(
            value = """
                SELECT u.usuario_id AS usuarioId,
                       u.codigo AS codigo,
                       u.usuario AS usuario,
                       u.dni AS dni,
                       u.nombres AS nombres,
                       u.ape_paterno AS apePaterno,
                       u.ape_materno AS apeMaterno,
                       CONCAT(
                           u.nombres,
                           ' ',
                           u.ape_paterno,
                           ' ',
                           u.ape_materno
                       ) AS nombreCompleto,
                       u.email AS correo,
                       MAX(r.rol_id) AS rolId,
                       MAX(r.nombre) AS nombreRol,
                       MAX(r.descripcion) AS descripcionRol,
                       u.activo AS estado,
                       CASE
                           WHEN u.activo = 1
                           THEN 'Activo'
                           ELSE 'Inactivo'
                       END AS estadoDsc
                FROM usuario u
                LEFT JOIN usuario_rol ur
                    ON ur.usuario_id = u.usuario_id
                    AND ur.activo = 1
                LEFT JOIN rol r
                    ON r.rol_id = ur.rol_id
                    AND r.activo = 1
                WHERE (
                    :usuario IS NULL
                    OR :usuario = ''
                    OR LOWER(u.usuario)
                       LIKE LOWER(CONCAT('%', :usuario, '%'))
                )
                AND (
                    :rol IS NULL
                    OR :rol = ''
                    OR LOWER(r.nombre)
                       LIKE LOWER(CONCAT('%', :rol, '%'))
                )
                AND (
                    :estado IS NULL
                    OR u.activo = :estado
                )
                GROUP BY
                    u.usuario_id,
                    u.codigo,
                    u.usuario,
                    u.dni,
                    u.nombres,
                    u.ape_paterno,
                    u.ape_materno,
                    u.email,
                    u.activo
                ORDER BY u.usuario_id
                """,
            countQuery = """
                SELECT COUNT(DISTINCT u.usuario_id)
                FROM usuario u
                LEFT JOIN usuario_rol ur
                    ON ur.usuario_id = u.usuario_id
                    AND ur.activo = 1
                LEFT JOIN rol r
                    ON r.rol_id = ur.rol_id
                    AND r.activo = 1
                WHERE (
                    :usuario IS NULL
                    OR :usuario = ''
                    OR LOWER(u.usuario)
                       LIKE LOWER(CONCAT('%', :usuario, '%'))
                )
                AND (
                    :rol IS NULL
                    OR :rol = ''
                    OR LOWER(r.nombre)
                       LIKE LOWER(CONCAT('%', :rol, '%'))
                )
                AND (
                    :estado IS NULL
                    OR u.activo = :estado
                )
                """,
            nativeQuery = true
    )
    Page<UsuarioProjection> listarUsuarios(
            @Param("usuario") String usuario,
            @Param("rol") String rol,
            @Param("estado") Boolean estado,
            Pageable pageable
    );

    boolean existsByDni(String dni);

    boolean existsByEmail(String email);

    boolean existsByEmailAndUsuarioIdNot(
            String email,
            Integer usuarioId
    );

    Optional<UsuarioEntity>
    findByUsuarioAndActivoTrue(String usuario);
}
