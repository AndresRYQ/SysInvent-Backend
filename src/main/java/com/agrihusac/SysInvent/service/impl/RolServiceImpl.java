package com.agrihusac.SysInvent.service.impl;

import com.agrihusac.SysInvent.model.entity.RolEntity;
import com.agrihusac.SysInvent.model.entity.RolModuloEntity;
import com.agrihusac.SysInvent.model.mapper.GlobalMapper;
import com.agrihusac.SysInvent.model.request.ActualizarRolRequest;
import com.agrihusac.SysInvent.model.request.RolRequest;
import com.agrihusac.SysInvent.model.response.RolResponse;
import com.agrihusac.SysInvent.repository.ModuloRepository;
import com.agrihusac.SysInvent.repository.RolRepository;
import com.agrihusac.SysInvent.repository.RolModuloRepository;
import com.agrihusac.SysInvent.service.RolService;
import com.agrihusac.SysInvent.utils.CustomPage;
import com.agrihusac.SysInvent.utils.MessageResponse;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService {

    private static final String MSG_ROL_REGISTRADO = "Rol registrado correctamente";
    private static final String MSG_ROL_ACTUALIZADO = "Rol actualizado correctamente";
    private static final String MSG_ROL_ACTIVADO = "Rol activado correctamente";
    private static final String MSG_ROL_DESACTIVADO = "Rol desactivado correctamente";
    private static final String MSG_ROL_NO_ENCONTRADO = "No se encontró el rol";

    private final RolRepository rolRepository;
    private final RolModuloRepository rolModuloRepository;
    private final ModuloRepository moduloRepository;
    private final GlobalMapper globalMapper;

    @Override
    @Transactional
    public ResponseEntity<MessageResponse> registrarRol(RolRequest request) {
        Set<Integer> moduloIds = new HashSet<>(request.getModulos());
        long modulosActivos = moduloRepository.findAllByModuloIdInAndActivoTrue(moduloIds).size();
        if (modulosActivos != moduloIds.size()) {
            return MessageResponse.setMessageResponse(Boolean.FALSE, HttpStatus.BAD_REQUEST,
                    "Uno o más módulos no existen o están inactivos");
        }

        RolEntity rol = RolEntity.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .activo(Boolean.TRUE)
                .build();
        rol = rolRepository.save(rol);
        sincronizarModulos(rol.getRolId(), moduloIds);
        return MessageResponse.setMessageResponse(Boolean.TRUE, HttpStatus.CREATED, MSG_ROL_REGISTRADO);
    }

    private void sincronizarModulos(Integer rolId, Set<Integer> moduloIds) {
        List<RolModuloEntity> relaciones = rolModuloRepository.findAllByRolId(rolId);
        Map<Integer, RolModuloEntity> relacionesPorModulo = new HashMap<>();

        for (RolModuloEntity relacion : relaciones) {
            relacionesPorModulo.put(relacion.getModuloId(), relacion);
            relacion.setActivo(moduloIds.contains(relacion.getModuloId()));
        }

        for (Integer moduloId : moduloIds) {
            if (!relacionesPorModulo.containsKey(moduloId)) {
                relaciones.add(RolModuloEntity.builder()
                        .rolId(rolId)
                        .moduloId(moduloId)
                        .activo(Boolean.TRUE)
                        .build());
            }
        }

        rolModuloRepository.saveAll(relaciones);
    }

    @Override
    @Transactional
    public ResponseEntity<MessageResponse> actualizarRol(ActualizarRolRequest request) {
        Set<Integer> moduloIds = new HashSet<>(request.getModulos());
        long modulosActivos = moduloRepository.findAllByModuloIdInAndActivoTrue(moduloIds).size();
        if (modulosActivos != moduloIds.size()) {
            return MessageResponse.setMessageResponse(
                    Boolean.FALSE,
                    HttpStatus.BAD_REQUEST,
                    "Uno o más módulos no existen o están inactivos");
        }

        RolEntity rol = rolRepository.findById(request.getRolId()).orElse(null);
        if (rol == null) {
            return MessageResponse.setMessageResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_ROL_NO_ENCONTRADO);
        }

        rol.setNombre(request.getNombre());
        rol.setDescripcion(request.getDescripcion());
        rolRepository.save(rol);
        sincronizarModulos(rol.getRolId(), moduloIds);

        return MessageResponse.setMessageResponse(Boolean.TRUE, HttpStatus.OK, MSG_ROL_ACTUALIZADO);
    }

    @Override
    @Transactional
    public ResponseEntity<MessageResponse> actualizarEstadoRol(Integer rolId, Boolean activo) {
        RolEntity rol = rolRepository.findById(rolId).orElse(null);

        if (rol == null) {
            return MessageResponse.setMessageResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_ROL_NO_ENCONTRADO);
        }

        rol.setActivo(activo);
        rolRepository.save(rol);

        String mensaje = Boolean.TRUE.equals(activo) ? MSG_ROL_ACTIVADO : MSG_ROL_DESACTIVADO;
        return MessageResponse.setMessageResponse(Boolean.TRUE, HttpStatus.OK, mensaje);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomPage<RolResponse> listarRoles(String nombre, Pageable pageable) {
        Page<RolResponse> roles = rolRepository.listarRoles(nombre, pageable)
                .map(projection -> globalMapper.map(projection, RolResponse.class));
        return new CustomPage<>(roles);
    }
}
