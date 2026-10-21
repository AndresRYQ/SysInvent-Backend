package com.agrihusac.SysInvent.service.bitacora.impl;

import com.agrihusac.SysInvent.model.entity.BitacoraEntity;
import com.agrihusac.SysInvent.model.request.BitacoraRequest;
import com.agrihusac.SysInvent.repository.BitacoraRepository;
import com.agrihusac.SysInvent.repository.RolRepository;
import com.agrihusac.SysInvent.repository.UsuarioRepository;
import com.agrihusac.SysInvent.service.bitacora.BitacoraService;
import com.agrihusac.SysInvent.utils.MessageResponse;
import io.jsonwebtoken.Claims;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BitacoraServiceImpl implements BitacoraService {
    private final BitacoraRepository bitacoraRepository;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    public BitacoraServiceImpl(BitacoraRepository bitacoraRepository,
                               UsuarioRepository usuarioRepository,
                               RolRepository rolRepository) {
        this.bitacoraRepository = bitacoraRepository;
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    @Override
    @Transactional
    public ResponseEntity<MessageResponse> registrar(BitacoraRequest request) {
        Integer usuarioId = usuarioIdDesdeJwt();
        if (usuarioId == null) {
            return MessageResponse.setResponse(false, HttpStatus.UNAUTHORIZED,
                    "El token no contiene un usuario válido");
        }
        if (!usuarioRepository.existsById(usuarioId)) {
            return MessageResponse.setResponse(false, HttpStatus.UNAUTHORIZED,
                    "El usuario del token no existe");
        }
        if (!rolRepository.existsById(request.getRolId())) {
            return MessageResponse.setResponse(false, HttpStatus.NOT_FOUND,
                    "El rol indicado no existe");
        }

        BitacoraEntity saved = bitacoraRepository.save(BitacoraEntity.builder()
                .fechaHora(LocalDateTime.now())
                .usuarioId(usuarioId)
                .rolId(request.getRolId())
                .modulo(request.getModulo().trim())
                .accion(request.getAccion())
                .detalle(request.getDetalle())
                .registroId(request.getRegistroId())
                .build());

        return MessageResponse.setResponse(true, HttpStatus.CREATED,
                "Registro de bitácora creado correctamente",
                Map.of("bitacoraId", saved.getBitacoraId()));
    }

    private Integer usuarioIdDesdeJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getDetails() instanceof Claims claims)) {
            return null;
        }
        Object claim = claims.get("usuarioId");
        if (claim instanceof Number number) {
            return number.intValue();
        }
        if (claim instanceof String value) {
            try {
                return Integer.valueOf(value);
            } catch (NumberFormatException ignored) {
                return null;
            }
        }
        return null;
    }
}
