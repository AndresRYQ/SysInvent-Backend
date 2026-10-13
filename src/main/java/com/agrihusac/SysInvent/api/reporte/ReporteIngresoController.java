package com.agrihusac.SysInvent.api.reporte;

import com.agrihusac.SysInvent.service.reporte.ReporteIngresoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Proposed route: GET /api/reportes/ingresos. The placeholder returns HTTP 501. */
@RestController
@RequestMapping("/api/reportes/ingresos")
@RequiredArgsConstructor
public class ReporteIngresoController {

    private final ReporteIngresoService reporteIngresoService;

    @GetMapping("/_plantilla")
    public void plantilla() {
        reporteIngresoService.operacionPendiente();
    }
}
