package com.agrihusac.SysInvent.api.reporte;

import com.agrihusac.SysInvent.service.reporte.ReporteProductoMasPedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Proposed route: GET /api/reportes/productos-mas-pedidos. The placeholder returns HTTP 501. */
@RestController
@RequestMapping("/api/reportes/productos-mas-pedidos")
@RequiredArgsConstructor
public class ReporteProductoMasPedidoController {

    private final ReporteProductoMasPedidoService reporteService;

    @GetMapping("/_plantilla")
    public void plantilla() {
        reporteService.operacionPendiente();
    }
}
