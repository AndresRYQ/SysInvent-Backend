package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.ProductoEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<ProductoEntity, Integer> {

    List<ProductoEntity> findAllByActivoTrueOrderByNombreAsc();

    @Query("""
            select p from ProductoEntity p
            join fetch p.unidadMedida
            where p.activo = true
              and p.tipoProductoId = :tipoProductoId
              and p.proveedorId = :proveedorId
            order by p.nombre asc
            """)
    List<ProductoEntity> findProductosParaCombo(@Param("tipoProductoId") Integer tipoProductoId,
                                                @Param("proveedorId") Integer proveedorId);

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

    Page<ProductoEntity> findByNombreContainingIgnoreCase(String filtro, Pageable pageable);

    Page<ProductoEntity> findByActivoTrueAndNombreContainingIgnoreCase(String filtro, Pageable pageable);

    boolean existsByCodigoIgnoreCase(String codigo);

    boolean existsByCodigoIgnoreCaseAndProductoIdNot(String codigo, Integer productoId);
}
