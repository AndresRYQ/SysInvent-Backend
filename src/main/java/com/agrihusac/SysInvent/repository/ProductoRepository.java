package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.ProductoEntity;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductoRepository extends JpaRepository<ProductoEntity, Integer> {

    /**
     * Incremento atomico del stock: evita perdidas de actualizaciones ante
     * ingresos concurrentes al mismo producto porque el calculo se ejecuta
     * en la base de datos dentro de la transaccion en curso.
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE ProductoEntity p SET p.stockActual = p.stockActual + :cantidad "
            + "WHERE p.productoId = :productoId AND p.activo = true")
    int incrementarStock(@Param("productoId") Integer productoId,
            @Param("cantidad") BigDecimal cantidad);
}
