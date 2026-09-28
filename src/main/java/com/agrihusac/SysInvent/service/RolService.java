package com.agrihusac.SysInvent.service;

import com.agrihusac.SysInvent.model.request.RolRequest;
import com.agrihusac.SysInvent.model.response.RolResponse;
import com.agrihusac.SysInvent.utils.CustomPage;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface RolService {

    ResponseEntity<Object> registrarRol(RolRequest request);

    CustomPage<RolResponse> listarRoles(String nombre, Pageable pageable);
}
