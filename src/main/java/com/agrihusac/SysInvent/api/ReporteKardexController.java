package com.agrihusac.SysInvent.api;

import com.agrihusac.SysInvent.model.request.reporte.ReporteKardexFiltro;
import com.agrihusac.SysInvent.model.response.reporte.ReporteKardexResponse;
import com.agrihusac.SysInvent.service.reporte.ReporteKardexService;
import com.agrihusac.SysInvent.utils.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reportes/kardex")
@RequiredArgsConstructor
@Validated
@Tag(name = "Reportes", description = "Reportes de inventario")
public class ReporteKardexController {

    private final ReporteKardexService reporteKardexService;

    @GetMapping
    @Operation(summary = "Generar reporte de Kardex",
            description = "Devuelve resumen, movimientos (más recientes primero) y stock actual. "
                    + "tipoMovimiento acepta ENTRADA, SALIDA o un tipo exacto del Kardex.")
    public ResponseEntity<Object> obtener(
            @RequestParam(required = false) @Min(1) Integer productoId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(required = false) String tipoMovimiento) {
        if (desde != null && hasta != null && desde.isAfter(hasta)) {
            return MessageResponse.setResponse(false, HttpStatus.BAD_REQUEST,
                    "La fecha inicial debe ser anterior o igual a la fecha final.");
        }
        ReporteKardexResponse reporte = reporteKardexService.generar(
                new ReporteKardexFiltro(productoId, desde, hasta, tipoMovimiento));
        String mensaje = reporte.movimientos().isEmpty()
                ? "No se encontraron movimientos para los filtros seleccionados."
                : "Reporte de Kardex generado correctamente.";
        return MessageResponse.setResponse(true, HttpStatus.OK, mensaje, reporte);
    }
}
