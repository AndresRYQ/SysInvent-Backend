package com.agrihusac.SysInvent.api;

import com.agrihusac.SysInvent.model.request.RolRequest;
import com.agrihusac.SysInvent.service.RolService;
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
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Tag(name = "Roles", description = "Operaciones de roles")
public class RolController {

    private final RolService rolService;

    @PostMapping
    @Operation(summary = "Registrar rol", description = "Registra un nuevo rol")
    public ResponseEntity<Object> registrarRol(@Valid @RequestBody RolRequest request) {
        return rolService.registrarRol(request);
    }
}
