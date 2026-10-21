package com.agrihusac.SysInvent.api;

import com.agrihusac.SysInvent.service.reporte.KardexService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/kardex")
@RequiredArgsConstructor
@Validated
@Tag(name = "Kardex", description = "Trazabilidad de movimientos y consulta de existencias")
public class KardexController {

    private final KardexService kardexService;

    @GetMapping("/movimientos")
    @Operation(summary = "Consultar movimientos del Kardex", description = "Filtra por producto, fechas y tipo de movimiento")
    public ResponseEntity<Object> movimientos(
            @RequestParam(required = false) @Min(1) Integer productoId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(required = false) String tipoMovimiento) {
        if (desde != null && hasta != null && desde.isAfter(hasta)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha inicial debe ser anterior o igual a la fecha final.");
        }
        return MessageResponse.setResponse(true, HttpStatus.OK,
                "Movimientos de Kardex obtenidos correctamente.",
                kardexService.buscar(productoId, desde, hasta, tipoMovimiento));
    }

    @GetMapping("/stock")
    @Operation(summary = "Consultar stock actual", description = "Use soloMinimo=true para listar productos en nivel mínimo o por debajo")
    public ResponseEntity<Object> stock(@RequestParam(defaultValue = "false") boolean soloMinimo) {
        return MessageResponse.setResponse(true, HttpStatus.OK,
                "Existencias obtenidas correctamente.", kardexService.consultarStock(soloMinimo));
    }
}
