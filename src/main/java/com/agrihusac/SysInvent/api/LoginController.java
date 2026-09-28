package com.agrihusac.SysInvent.api;

import com.agrihusac.SysInvent.model.request.LoginRequest;
import com.agrihusac.SysInvent.service.LoginService;
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
@RequestMapping("/api/login")
@RequiredArgsConstructor
@Tag(name = "Login", description = "Autenticación de usuarios")
public class LoginController {

    private final LoginService loginService;

    @PostMapping
    @Operation(summary = "Iniciar sesión", description = "Valida las credenciales del usuario")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequest request) {
        return loginService.login(request);
    }
}
