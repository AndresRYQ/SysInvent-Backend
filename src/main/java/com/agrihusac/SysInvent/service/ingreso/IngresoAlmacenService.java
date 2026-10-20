package com.agrihusac.SysInvent.service.ingreso;

import com.agrihusac.SysInvent.model.request.ingreso.IngresoAlmacenRequest;
import com.agrihusac.SysInvent.model.response.ingreso.IngresoAlmacenResponse;
import com.agrihusac.SysInvent.utils.CustomPage;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface IngresoAlmacenService {

    ResponseEntity<Object> registrar(IngresoAlmacenRequest request);

    ResponseEntity<Object> obtener(Integer id);

    CustomPage<IngresoAlmacenResponse> listar(String numeroIngreso, Pageable pageable);
}
