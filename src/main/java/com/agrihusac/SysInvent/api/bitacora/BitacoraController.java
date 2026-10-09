package com.agrihusac.SysInvent.api.bitacora;

import com.agrihusac.SysInvent.service.bitacora.BitacoraService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Proposed route base: /api/bitacora. The placeholder returns HTTP 501. */
@RestController
@RequestMapping("/api/bitacora")
@RequiredArgsConstructor
public class BitacoraController {

    private final BitacoraService bitacoraService;

    @GetMapping("/_plantilla")
    public void plantilla() {
        bitacoraService.operacionPendiente();
    }
}
