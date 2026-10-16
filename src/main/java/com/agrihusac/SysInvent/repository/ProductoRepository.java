package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

import java.math.BigDecimal;

public interface ProductoRepository extends JpaRepository<ProductoEntity, Integer> {

        @Lock(LockModeType.PESSIMISTIC_WRITE)
        @Query("select p from ProductoEntity p where p.productoId = :productoId")
        Optional<ProductoEntity> buscarParaActualizar(@Param("productoId") Integer productoId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update ProductoEntity p
               set p.stockActual = p.stockActual - :cantidad
             where p.productoId = :productoId
               and p.activo = true
               and p.stockActual >= :cantidad
            """)
    int descontarStockDisponible(@Param("productoId") Integer productoId,
                                 @Param("cantidad") BigDecimal cantidad);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update ProductoEntity p
               set p.stockActual = p.stockActual + :cantidad
             where p.productoId = :productoId
            """)
    int incrementarStock(@Param("productoId") Integer productoId,
                         @Param("cantidad") BigDecimal cantidad);
}
