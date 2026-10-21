package com.agrihusac.SysInvent.service.bitacora;

import com.agrihusac.SysInvent.model.request.BitacoraRequest;
import com.agrihusac.SysInvent.utils.MessageResponse;
import org.springframework.http.ResponseEntity;

public interface BitacoraService {

    ResponseEntity<MessageResponse> registrar(BitacoraRequest request);
}
