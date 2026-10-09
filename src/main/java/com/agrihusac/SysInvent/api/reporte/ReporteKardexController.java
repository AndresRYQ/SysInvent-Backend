package com.agrihusac.SysInvent.api.reporte;

import com.agrihusac.SysInvent.service.reporte.ReporteKardexService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Proposed route: GET /api/reportes/kardex. The placeholder returns HTTP 501. */
@RestController
@RequestMapping("/api/reportes/kardex")
@RequiredArgsConstructor
public class ReporteKardexController {

    private final ReporteKardexService reporteKardexService;

    @GetMapping("/_plantilla")
    public void plantilla() {
        reporteKardexService.operacionPendiente();
    }
}
