package com.agrihusac.SysInvent.service.bitacora.impl;

import com.agrihusac.SysInvent.model.entity.BitacoraEntity;
import com.agrihusac.SysInvent.model.request.BitacoraRequest;
import com.agrihusac.SysInvent.model.projection.BitacoraProjection;
import com.agrihusac.SysInvent.repository.BitacoraRepository;
import com.agrihusac.SysInvent.repository.RolRepository;
import com.agrihusac.SysInvent.repository.UsuarioRepository;
import com.agrihusac.SysInvent.service.bitacora.BitacoraService;
import com.agrihusac.SysInvent.utils.MessageResponse;
import io.jsonwebtoken.Claims;
import java.time.LocalDateTime;
import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;
import com.agrihusac.SysInvent.utils.CustomPage;
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
        Integer usuarioId = claimInteger("usuarioId");
        Integer rolId = claimInteger("rolId");
        if (usuarioId == null) {
            return MessageResponse.setResponse(false, HttpStatus.UNAUTHORIZED,
                    "El token no contiene un usuario valido");
        }
        if (!usuarioRepository.existsById(usuarioId)) {
            return MessageResponse.setResponse(false, HttpStatus.UNAUTHORIZED,
                    "El usuario del token no existe");
        }
        if (rolId == null) {
            return MessageResponse.setResponse(false, HttpStatus.UNAUTHORIZED,
                    "El token no contiene un rol valido");
        }
        if (!rolRepository.existsById(rolId)) {
            return MessageResponse.setResponse(false, HttpStatus.NOT_FOUND,
                    "El rol del usuario no existe");
        }

        bitacoraRepository.save(BitacoraEntity.builder()
                .fechaHora(LocalDateTime.now())
                .usuarioId(usuarioId)
                .rolId(rolId)
                .modulo(request.getModulo().trim())
                .accion(request.getAccion())
                .detalle(request.getDetalle())
                .registroId(request.getRegistroId())
                .build());

        return MessageResponse.setResponse(true, HttpStatus.CREATED,
                "Registro de bitacora creado correctamente");
    }

    @Override
    @Transactional(readOnly = true)
    public CustomPage<BitacoraProjection> listar(String busqueda, String modulo, String accion,
                                                 LocalDate fechaDesde, LocalDate fechaHasta, Pageable pageable) {
        return new CustomPage<>(bitacoraRepository.listar(busqueda, modulo, accion, fechaDesde, fechaHasta, pageable));
    }

    private Integer claimInteger(String name) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getDetails() instanceof Claims claims)) {
            return null;
        }
        Object claim = claims.get(name);
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
