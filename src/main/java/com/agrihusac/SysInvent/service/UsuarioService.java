package com.agrihusac.SysInvent.service;

import com.agrihusac.SysInvent.model.request.ActualizarUsuarioRequest;
import com.agrihusac.SysInvent.model.request.UsuarioRequest;
import com.agrihusac.SysInvent.model.response.UsuarioResponse;
import com.agrihusac.SysInvent.utils.CustomPage;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface UsuarioService {

    CustomPage<UsuarioResponse> listarUsuarios(String usuario, String rol, Boolean estado, Pageable pageable);

    ResponseEntity<Object> registrarUsuario(UsuarioRequest request);

    ResponseEntity<Object> actualizarUsuario(ActualizarUsuarioRequest request);

    ResponseEntity<Object> actualizarEstadoUsuario(Integer idUsuario, Boolean activo);
}
