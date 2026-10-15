package com.agrihusac.SysInvent.service;

import com.agrihusac.SysInvent.model.request.RolRequest;
import com.agrihusac.SysInvent.model.request.ActualizarRolRequest;
import com.agrihusac.SysInvent.model.response.RolResponse;
import com.agrihusac.SysInvent.utils.CustomPage;
import com.agrihusac.SysInvent.utils.MessageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface RolService {

    ResponseEntity<MessageResponse> registrarRol(RolRequest request);

    ResponseEntity<MessageResponse> actualizarRol(ActualizarRolRequest request);

    ResponseEntity<MessageResponse> actualizarEstadoRol(Integer rolId, Boolean activo);

    CustomPage<RolResponse> listarRoles(String nombre, Pageable pageable);
}
