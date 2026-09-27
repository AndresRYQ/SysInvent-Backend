package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.SaludoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SaludoRepository extends JpaRepository<SaludoEntity, Long> {

    Optional<SaludoEntity> findTopByOrderByIdAsc();
}
