package com.agrihusac.SysInvent.api;

import com.agrihusac.SysInvent.model.response.reporte.ReporteFiltrosResponse;
import com.agrihusac.SysInvent.service.reporte.ReporteFiltrosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reportes/filtros")
@RequiredArgsConstructor
@Tag(name = "Reportes", description = "Reportes de inventario")
public class ReporteFiltrosController {

    private final ReporteFiltrosService reporteFiltrosService;

    @GetMapping
    @Operation(summary = "Catálogos para los filtros de reportes",
            description = "Proveedores, centros de costo, destinos, partes de equipo, tipos de producto, "
                    + "categorías y productos activos.")
    public ReporteFiltrosResponse obtener() {
        return reporteFiltrosService.obtener();
    }
}
