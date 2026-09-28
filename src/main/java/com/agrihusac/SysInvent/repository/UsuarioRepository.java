package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.UsuarioEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {

    boolean existsByDni(String dni);

    boolean existsByEmail(String email);

    boolean existsByEmailAndUsuarioIdNot(String email, Integer usuarioId);

    Optional<UsuarioEntity> findByUsuarioAndActivoTrue(String usuario);
}
