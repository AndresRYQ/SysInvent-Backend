package com.agrihusac.SysInvent.service.impl;

import com.agrihusac.SysInvent.model.entity.UsuarioEntity;
import com.agrihusac.SysInvent.model.entity.UsuarioRolEntity;
import com.agrihusac.SysInvent.model.request.LoginRequest;
import com.agrihusac.SysInvent.repository.RolRepository;
import com.agrihusac.SysInvent.repository.UsuarioRepository;
import com.agrihusac.SysInvent.repository.UsuarioRolRepository;
import com.agrihusac.SysInvent.service.JwtService;
import com.agrihusac.SysInvent.service.LoginService;
import com.agrihusac.SysInvent.utils.MessageResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private static final String MSG_CREDENCIALES_INVALIDAS = "Usuario o contrasena incorrectos";
    private static final String MSG_LOGIN_CORRECTO = "Inicio de sesion correcto";

    private final UsuarioRepository usuarioRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public ResponseEntity<Object> login(LoginRequest request) {
        UsuarioEntity usuario = usuarioRepository.findByUsuarioAndActivoTrue(request.getUsuario()).orElse(null);

        if (usuario == null || !passwordEncoder.matches(request.getContrasena(), usuario.getContrasena())) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.UNAUTHORIZED, MSG_CREDENCIALES_INVALIDAS);
        }

        List<Integer> rolesIds = usuarioRolRepository.findAllByUsuarioIdAndActivoTrue(usuario.getUsuarioId())
                .stream()
                .map(UsuarioRolEntity::getRolId)
                .toList();
        List<Map<String, Object>> roles = new ArrayList<>();
        if (!rolesIds.isEmpty()) {
            roles = rolRepository.findAllByRolIdInAndActivoTrue(rolesIds)
                    .stream()
                    .map(rol -> {
                        Map<String, Object> role = new HashMap<>();
                        role.put("rolId", rol.getRolId());
                        role.put("nombre", rol.getNombre());
                        return role;
                    })
                    .toList();
        }
        String token = jwtService.generateToken(usuario, roles);
        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, MSG_LOGIN_CORRECTO, token);
    }
}
