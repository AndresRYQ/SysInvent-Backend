package com.agrihusac.SysInvent.service.impl;

import com.agrihusac.SysInvent.model.entity.UsuarioEntity;
import com.agrihusac.SysInvent.model.entity.UsuarioRolEntity;
import com.agrihusac.SysInvent.model.request.ActualizarUsuarioRequest;
import com.agrihusac.SysInvent.model.request.UsuarioRequest;
import com.agrihusac.SysInvent.repository.RolRepository;
import com.agrihusac.SysInvent.repository.UsuarioRepository;
import com.agrihusac.SysInvent.repository.UsuarioRolRepository;
import com.agrihusac.SysInvent.service.UsuarioService;
import com.agrihusac.SysInvent.utils.MessageResponse;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String MSG_USUARIO_REGISTRADO = "Usuario registrado correctamente";
    private static final String MSG_USUARIO_ACTUALIZADO = "Usuario actualizado correctamente";
    private static final String MSG_USUARIO_ACTIVADO = "Usuario activado correctamente";
    private static final String MSG_USUARIO_DESACTIVADO = "Usuario desactivado correctamente";
    private static final String MSG_USUARIO_NO_ENCONTRADO = "No se encontro el usuario";
    private static final String MSG_DNI_EXISTENTE = "El DNI ya se encuentra registrado";
    private static final String MSG_EMAIL_EXISTENTE = "El email ya se encuentra registrado";
    private static final String MSG_ROL_NO_ENCONTRADO = "Uno o mas roles no existen o estan inactivos";
    private static final String MSG_ROL_DUPLICADO = "La lista de roles contiene valores duplicados";

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public ResponseEntity<Object> registrarUsuario(UsuarioRequest request) {
        if (usuarioRepository.existsByDni(request.getDni())) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_DNI_EXISTENTE);
        }

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_EMAIL_EXISTENTE);
        }

        Set<Integer> rolesIds = new HashSet<>(request.getRolesIds());
        if (rolesIds.size() != request.getRolesIds().size()) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.BAD_REQUEST, MSG_ROL_DUPLICADO);
        }

        if (rolRepository.findAllByRolIdInAndActivoTrue(rolesIds).size() != rolesIds.size()) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_ROL_NO_ENCONTRADO);
        }

        String contrasenaTemporal = generarContrasena();
        log.debug("Contraseña temporal generada para el usuario {}: {}", request.getEmail(), contrasenaTemporal);
        UsuarioEntity usuario = UsuarioEntity.builder()
                .usuario(request.getDni())
                .nombres(request.getNombres())
                .apePaterno(request.getApePaterno())
                .apeMaterno(request.getApeMaterno())
                .dni(request.getDni())
                .email(request.getEmail())
                .contrasena(passwordEncoder.encode(contrasenaTemporal))
                .activo(Boolean.TRUE)
                .build();

        usuarioRepository.save(usuario);

        rolesIds.forEach(rolId -> usuarioRolRepository.save(UsuarioRolEntity.builder()
                .usuarioId(usuario.getUsuarioId())
                .rolId(rolId)
                .activo(Boolean.TRUE)
                .fechaAsignacion(LocalDate.now())
                .build()));

        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.CREATED, MSG_USUARIO_REGISTRADO);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> actualizarUsuario(ActualizarUsuarioRequest request) {
        UsuarioEntity usuario = usuarioRepository.findById(request.getUsuarioId()).orElse(null);
        if (usuario == null) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_USUARIO_NO_ENCONTRADO);
        }

        if (usuarioRepository.existsByEmailAndUsuarioIdNot(request.getEmail(), request.getUsuarioId())) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_EMAIL_EXISTENTE);
        }

        Set<Integer> rolesIds = new HashSet<>(request.getRolesIds());
        if (rolesIds.size() != request.getRolesIds().size()) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.BAD_REQUEST, MSG_ROL_DUPLICADO);
        }

        if (rolRepository.findAllByRolIdInAndActivoTrue(rolesIds).size() != rolesIds.size()) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_ROL_NO_ENCONTRADO);
        }

        usuario.setNombres(request.getNombres());
        usuario.setApePaterno(request.getApePaterno());
        usuario.setApeMaterno(request.getApeMaterno());
        usuario.setEmail(request.getEmail());
        usuarioRepository.save(usuario);

        List<UsuarioRolEntity> rolesAsignados = usuarioRolRepository.findAllByUsuarioId(usuario.getUsuarioId());
        Set<Integer> rolesExistentes = new HashSet<>();

        rolesAsignados.forEach(usuarioRol -> {
            rolesExistentes.add(usuarioRol.getRolId());
            usuarioRol.setActivo(rolesIds.contains(usuarioRol.getRolId()));
        });
        usuarioRolRepository.saveAll(rolesAsignados);

        rolesIds.stream()
                .filter(rolId -> !rolesExistentes.contains(rolId))
                .forEach(rolId -> usuarioRolRepository.save(UsuarioRolEntity.builder()
                        .usuarioId(usuario.getUsuarioId())
                        .rolId(rolId)
                        .activo(Boolean.TRUE)
                        .fechaAsignacion(LocalDate.now())
                        .build()));

        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, MSG_USUARIO_ACTUALIZADO);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> actualizarEstadoUsuario(Integer idUsuario, Boolean activo) {
        UsuarioEntity usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_USUARIO_NO_ENCONTRADO);
        }

        usuario.setActivo(activo);
        usuarioRepository.save(usuario);

        String mensaje = activo ? MSG_USUARIO_ACTIVADO : MSG_USUARIO_DESACTIVADO;
        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, mensaje);
    }

    private String generarContrasena() {
        return String.format("%07d", SECURE_RANDOM.nextInt(10_000_000));
    }
}
