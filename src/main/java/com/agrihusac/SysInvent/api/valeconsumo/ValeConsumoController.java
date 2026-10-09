package com.agrihusac.SysInvent.api.valeconsumo;

import com.agrihusac.SysInvent.service.valeconsumo.ValeConsumoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Proposed route base: /api/vales-consumo. The placeholder returns HTTP 501. */
@RestController
@RequestMapping("/api/vales-consumo")
@RequiredArgsConstructor
public class ValeConsumoController {

    private final ValeConsumoService valeConsumoService;

    @GetMapping("/_plantilla")
    public void plantilla() {
        valeConsumoService.operacionPendiente();
    }
}
