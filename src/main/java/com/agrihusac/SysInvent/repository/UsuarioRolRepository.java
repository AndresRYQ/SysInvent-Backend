package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.UsuarioRolEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRolRepository extends JpaRepository<UsuarioRolEntity, Integer> {

    List<UsuarioRolEntity> findAllByUsuarioId(Integer usuarioId);

    List<UsuarioRolEntity> findAllByUsuarioIdAndActivoTrue(Integer usuarioId);
}
