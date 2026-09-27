package com.agrihusac.SysInvent.api;

import com.agrihusac.SysInvent.model.request.SaludoRequest;
import com.agrihusac.SysInvent.model.response.SaludoResponse;
import com.agrihusac.SysInvent.service.SaludoService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HealthController {

    private final SaludoService saludoService;

    @PostMapping("/hola-mundo")
    public SaludoResponse holaMundo(@Valid @RequestBody SaludoRequest request) {
        return saludoService.crearSaludo(request);
    }
}
