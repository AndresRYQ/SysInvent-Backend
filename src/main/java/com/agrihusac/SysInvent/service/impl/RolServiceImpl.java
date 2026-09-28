package com.agrihusac.SysInvent.service.impl;

import com.agrihusac.SysInvent.model.entity.RolEntity;
import com.agrihusac.SysInvent.model.request.RolRequest;
import com.agrihusac.SysInvent.repository.RolRepository;
import com.agrihusac.SysInvent.service.RolService;
import com.agrihusac.SysInvent.utils.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService {

    private static final String MSG_ROL_REGISTRADO = "Rol registrado correctamente";
    private static final String MSG_ROL_ACTUALIZADO = "Rol actualizado correctamente";
    private static final String MSG_ROL_NO_ENCONTRADO = "No se encontró el rol";

    private final RolRepository rolRepository;

    @Override
    @Transactional
    public ResponseEntity<Object> registrarRol(RolRequest request) {
        RolEntity rol;
        HttpStatus status;
        String mensaje;

        if (request.getRolId() == 0) {
            rol = RolEntity.builder()
                    .nombre(request.getNombre())
                    .descripcion(request.getDescripcion())
                    .activo(Boolean.TRUE)
                    .build();
            status = HttpStatus.CREATED;
            mensaje = MSG_ROL_REGISTRADO;
        } else {
            rol = rolRepository.findById(request.getRolId()).orElse(null);

            if (rol == null) {
                return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_ROL_NO_ENCONTRADO);
            }

            rol.setNombre(request.getNombre());
            rol.setDescripcion(request.getDescripcion());
            status = HttpStatus.OK;
            mensaje = MSG_ROL_ACTUALIZADO;
        }

        rolRepository.save(rol);
        return MessageResponse.setResponse(Boolean.TRUE, status, mensaje);
    }
}
