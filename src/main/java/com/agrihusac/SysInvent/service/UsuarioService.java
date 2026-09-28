package com.agrihusac.SysInvent.service;

import com.agrihusac.SysInvent.model.request.ActualizarUsuarioRequest;
import com.agrihusac.SysInvent.model.request.UsuarioRequest;
import org.springframework.http.ResponseEntity;

public interface UsuarioService {

    ResponseEntity<Object> registrarUsuario(UsuarioRequest request);

    ResponseEntity<Object> actualizarUsuario(ActualizarUsuarioRequest request);
}
