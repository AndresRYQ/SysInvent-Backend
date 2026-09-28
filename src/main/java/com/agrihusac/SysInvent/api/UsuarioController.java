package com.agrihusac.SysInvent.api;

import com.agrihusac.SysInvent.model.request.UsuarioRequest;
import com.agrihusac.SysInvent.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
