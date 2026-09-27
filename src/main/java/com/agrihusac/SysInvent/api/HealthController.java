package com.agrihusac.SysInvent.api;

import com.agrihusac.SysInvent.model.request.SaludoRequest;
import com.agrihusac.SysInvent.model.response.SaludoResponse;
import com.agrihusac.SysInvent.service.SaludoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Saludo", description = "Endpoints de prueba")
public class HealthController {

    private final SaludoService saludoService;

    @PostMapping("/hola-mundo")
    @Operation(summary = "Crear saludo", description = "Crea un saludo de prueba y devuelve la respuesta generada")
    public SaludoResponse holaMundo(@Valid @RequestBody SaludoRequest request) {
        return saludoService.crearSaludo(request);
    }
}
