package com.agrihusac.SysInvent.service.catalogo;

import com.agrihusac.SysInvent.utils.CustomPage;
import com.agrihusac.SysInvent.utils.MessageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface CatalogoService<Q, R> {

    ResponseEntity<MessageResponse> crear(Q request);

    ResponseEntity<MessageResponse> actualizar(Integer id, Q request);

    ResponseEntity<MessageResponse> obtener(Integer id);

    CustomPage<R> listar(String filtro, Pageable pageable);

    ResponseEntity<MessageResponse> cambiarEstado(Integer id, boolean activo);
}
