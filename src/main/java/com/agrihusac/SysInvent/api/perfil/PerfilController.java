package com.agrihusac.SysInvent.api.perfil;

import com.agrihusac.SysInvent.service.perfil.PerfilService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Proposed route: GET /api/usuarios/perfil. User identity and response contract are pending. */
@RestController
@RequestMapping("/api/usuarios/perfil")
@RequiredArgsConstructor
public class PerfilController {

    private final PerfilService perfilService;

    @GetMapping("/_plantilla")
    public void plantilla() {
        perfilService.operacionPendiente();
    }
}
