package com.agrihusac.SysInvent.api;

import com.agrihusac.SysInvent.service.reporte.ReporteIngresoService;
import com.agrihusac.SysInvent.model.response.reporte.ReporteIngresoListadoResponse;
import com.agrihusac.SysInvent.utils.CustomPage;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reportes/ingresos")
@RequiredArgsConstructor
@Validated
public class ReporteIngresoController {

    private final ReporteIngresoService reporteIngresoService;

    @GetMapping
    public CustomPage<ReporteIngresoListadoResponse> listar(
            @RequestParam(required = false) String busqueda,
            @RequestParam(required = false) @Min(1) Integer proveedorId,
            @RequestParam(required = false) @Min(1) Integer tipoProductoId,
            @RequestParam(required = false) @Min(1) Integer productoId,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(defaultValue = "1") @Min(1) Integer pagina,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer tamPagina) {
        if (fechaDesde != null && fechaHasta != null && fechaDesde.isAfter(fechaHasta)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La fecha desde no puede ser posterior a la fecha hasta");
        }
        Pageable pageable = PageRequest.of(pagina - 1, tamPagina);
        return reporteIngresoService.listar(busqueda, proveedorId, tipoProductoId, productoId,
                estado, fechaDesde, fechaHasta, pageable);
    }
}
