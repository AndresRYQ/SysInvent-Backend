package com.agrihusac.SysInvent.service.impl;

import com.agrihusac.SysInvent.model.entity.UsuarioEntity;
import com.agrihusac.SysInvent.model.request.LoginRequest;
import com.agrihusac.SysInvent.model.response.LoginResponse;
import com.agrihusac.SysInvent.repository.UsuarioRepository;
import com.agrihusac.SysInvent.service.LoginService;
import com.agrihusac.SysInvent.utils.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private static final String MSG_CREDENCIALES_INVALIDAS = "Usuario o contrasena incorrectos";
    private static final String MSG_LOGIN_CORRECTO = "Inicio de sesion correcto";

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Object> login(LoginRequest request) {
        UsuarioEntity usuario = usuarioRepository.findByUsuarioAndActivoTrue(request.getUsuario()).orElse(null);

        if (usuario == null || !passwordEncoder.matches(request.getContrasena(), usuario.getContrasena())) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.UNAUTHORIZED, MSG_CREDENCIALES_INVALIDAS);
        }

        LoginResponse response = LoginResponse.builder()
                .usuarioId(usuario.getUsuarioId())
                .usuario(usuario.getUsuario())
                .nombres(usuario.getNombres())
                .apePaterno(usuario.getApePaterno())
                .apeMaterno(usuario.getApeMaterno())
                .email(usuario.getEmail())
                .build();

        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, MSG_LOGIN_CORRECTO, response);
    }
}
