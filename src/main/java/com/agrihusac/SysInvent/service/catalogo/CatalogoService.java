package com.agrihusac.SysInvent.service.catalogo;

import com.agrihusac.SysInvent.utils.CustomPage;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface CatalogoService<Q, R> {

    ResponseEntity<Object> crear(Q request);

    ResponseEntity<Object> actualizar(Integer id, Q request);

    ResponseEntity<Object> obtener(Integer id);

    CustomPage<R> listar(String filtro, Pageable pageable);

    ResponseEntity<Object> cambiarEstado(Integer id, boolean activo);
}
