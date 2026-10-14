package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.RolModuloEntity;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolModuloRepository extends JpaRepository<RolModuloEntity, Integer> {

    List<RolModuloEntity> findAllByRolIdInAndActivoTrue(Collection<Integer> rolIds);
}
