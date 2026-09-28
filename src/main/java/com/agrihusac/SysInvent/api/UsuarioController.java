package com.agrihusac.SysInvent.api;

import com.agrihusac.SysInvent.model.request.ActualizarUsuarioRequest;
import com.agrihusac.SysInvent.model.request.UsuarioRequest;
import com.agrihusac.SysInvent.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Operaciones de usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    @Operation(summary = "Registrar usuario", description = "Registra un usuario con una contraseña generada")
    public ResponseEntity<Object> registrarUsuario(@Valid @RequestBody UsuarioRequest request) {
        return usuarioService.registrarUsuario(request);
    }

    @PutMapping
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos personales del usuario")
    public ResponseEntity<Object> actualizarUsuario(@Valid @RequestBody ActualizarUsuarioRequest request) {
        return usuarioService.actualizarUsuario(request);
    }

    @DeleteMapping("/estado")
    @Operation(summary = "Actualizar estado del usuario", description = "Activa o desactiva un usuario")
    public ResponseEntity<Object> actualizarEstadoUsuario(
            @NotNull(message = "{message.required}") @RequestParam("idUsuario") Integer idUsuario,
            @NotNull(message = "{message.required}") @RequestParam("activo") Boolean activo) {
        return usuarioService.actualizarEstadoUsuario(idUsuario, activo);
    }
}
