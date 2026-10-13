package com.agrihusac.SysInvent.api.permiso;

import com.agrihusac.SysInvent.service.permiso.PermisoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Proposed route base: /api/permisos. The placeholder returns HTTP 501. */
@RestController
@RequestMapping("/api/permisos")
@RequiredArgsConstructor
public class PermisoController {

    private final PermisoService permisoService;

    @GetMapping("/_plantilla")
    public void plantilla() {
        permisoService.operacionPendiente();
    }
}
