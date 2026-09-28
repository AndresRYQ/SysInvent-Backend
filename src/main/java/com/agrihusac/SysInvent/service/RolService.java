package com.agrihusac.SysInvent.service;

import com.agrihusac.SysInvent.model.request.RolRequest;
import org.springframework.http.ResponseEntity;

public interface RolService {

    ResponseEntity<Object> registrarRol(RolRequest request);
}
