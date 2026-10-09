package com.agrihusac.SysInvent.api.reporte;

import com.agrihusac.SysInvent.service.reporte.ReporteValeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Proposed route: GET /api/reportes/vales. The placeholder returns HTTP 501. */
@RestController
@RequestMapping("/api/reportes/vales")
@RequiredArgsConstructor
public class ReporteValeController {

    private final ReporteValeService reporteValeService;

    @GetMapping("/_plantilla")
    public void plantilla() {
        reporteValeService.operacionPendiente();
    }
}
