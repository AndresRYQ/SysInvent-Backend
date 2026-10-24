package com.agrihusac.SysInvent.service.impl;

import com.agrihusac.SysInvent.model.entity.UsuarioEntity;
import com.agrihusac.SysInvent.model.entity.ModuloEntity;
import com.agrihusac.SysInvent.model.entity.RolModuloEntity;
import com.agrihusac.SysInvent.model.entity.UsuarioRolEntity;
import com.agrihusac.SysInvent.model.request.LoginRequest;
import com.agrihusac.SysInvent.model.response.auth.LoginResponse;
import com.agrihusac.SysInvent.repository.ModuloRepository;
import com.agrihusac.SysInvent.repository.RolRepository;
import com.agrihusac.SysInvent.repository.RolModuloRepository;
import com.agrihusac.SysInvent.repository.UsuarioRepository;
import com.agrihusac.SysInvent.repository.UsuarioRolRepository;
import com.agrihusac.SysInvent.service.JwtService;
import com.agrihusac.SysInvent.service.LoginService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private static final int ADMIN_ROLE_ID = 1;

    private final UsuarioRepository usuarioRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final RolRepository rolRepository;
    private final RolModuloRepository rolModuloRepository;
    private final ModuloRepository moduloRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        UsuarioEntity usuario = usuarioRepository.findByUsuarioAndActivoTrue(request.getUsuario()).orElse(null);

        if (usuario == null || !passwordEncoder.matches(request.getContrasena(), usuario.getContrasena())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(false, MSG_CREDENCIALES_INVALIDAS,
                            HttpStatus.UNAUTHORIZED.value(), null));
        }

        List<Integer> rolesIds = usuarioRolRepository.findAllByUsuarioIdAndActivoTrue(usuario.getUsuarioId())
                .stream()
                .map(UsuarioRolEntity::getRolId)
                .filter(java.util.Objects::nonNull)
                .distinct()
                .toList();

        if (rolesIds.size() != 1) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new LoginResponse(false,
                            "El usuario debe tener exactamente un rol activo",
                            HttpStatus.FORBIDDEN.value(), null));
        }

        List<RolModuloEntity> relacionesRolModulo = rolesIds.isEmpty()
                ? List.of()
                : rolModuloRepository.findAllByRolIdInAndActivoTrue(rolesIds);

        Map<Integer, List<Integer>> moduloIdsPorRol = relacionesRolModulo.stream()
                .filter(relacion -> relacion.getRolId() != null && relacion.getModuloId() != null)
                .collect(java.util.stream.Collectors.groupingBy(
                        RolModuloEntity::getRolId,
                        LinkedHashMap::new,
                        java.util.stream.Collectors.mapping(
                                RolModuloEntity::getModuloId,
                                java.util.stream.Collectors.toList())));

        boolean administrador = rolesIds.contains(ADMIN_ROLE_ID);
        List<ModuloEntity> modulosActivos = administrador
                ? List.of()
                : obtenerModulosAsignados(relacionesRolModulo);

        Map<Integer, ModuloEntity> modulosPorId = modulosActivos.stream()
                .collect(java.util.stream.Collectors.toMap(
                        ModuloEntity::getModuloId,
                        modulo -> modulo,
                        (primero, segundo) -> primero,
                        LinkedHashMap::new));

        List<Map<String, Object>> modulos = new ArrayList<>();
        Set<Integer> moduloIdsIncluidos = new LinkedHashSet<>();

        Integer rolId = rolesIds.get(0);
        List<Integer> moduloIds = administrador
                ? List.of()
                : moduloIdsPorRol.getOrDefault(rolId, List.of());

        for (Integer moduloId : moduloIds) {
            if (moduloIdsIncluidos.add(moduloId) && modulosPorId.containsKey(moduloId)) {
                modulos.add(mapearModulo(modulosPorId.get(moduloId)));
            }
        }

        String token = jwtService.generateToken(usuario, rolId, modulos);
        return ResponseEntity.ok(new LoginResponse(true, MSG_LOGIN_CORRECTO,
                HttpStatus.OK.value(), token));
    }

    private List<ModuloEntity> obtenerModulosAsignados(List<RolModuloEntity> relacionesRolModulo) {
        List<Integer> moduloIds = relacionesRolModulo.stream()
                .map(RolModuloEntity::getModuloId)
                .filter(java.util.Objects::nonNull)
                .distinct()
                .toList();
        return moduloIds.isEmpty()
                ? List.of()
                : moduloRepository.findAllByModuloIdInAndActivoTrue(moduloIds);
    }

    private Map<String, Object> mapearModulo(ModuloEntity modulo) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("moduloId", modulo.getModuloId());
        data.put("nombre", modulo.getNombre());
        data.put("ruta", modulo.getRuta());
        return data;
    }
}
