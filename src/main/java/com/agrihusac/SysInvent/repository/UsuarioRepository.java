package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {

    boolean existsByDni(String dni);
}
