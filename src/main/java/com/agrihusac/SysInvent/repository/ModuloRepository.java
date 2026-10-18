package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.ModuloEntity;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuloRepository extends JpaRepository<ModuloEntity, Integer> {

    List<ModuloEntity> findAllByActivoTrueOrderByModuloIdAsc();

    List<ModuloEntity> findAllByModuloIdInAndActivoTrue(Collection<Integer> moduloIds);
}
