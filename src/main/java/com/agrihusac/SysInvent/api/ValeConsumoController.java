package com.agrihusac.SysInvent.api;

import com.agrihusac.SysInvent.model.request.valeconsumo.ValeConsumoRequest;
import com.agrihusac.SysInvent.service.valeconsumo.ValeConsumoService;
import com.agrihusac.SysInvent.model.response.valeconsumo.ValeConsumoListadoResponse;
import com.agrihusac.SysInvent.utils.CustomPage;
import com.agrihusac.SysInvent.utils.MessageResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/vales-consumo")
@RequiredArgsConstructor
@Validated
public class ValeConsumoController {

    private final ValeConsumoService valeConsumoService;

    @GetMapping
    public CustomPage<ValeConsumoListadoResponse> listar(
            @RequestParam(required = false) String busqueda,
            @RequestParam(required = false) @Min(1) Integer centroCostoId,
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
        return valeConsumoService.listar(busqueda, centroCostoId, estado,
                fechaDesde, fechaHasta, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obtener(@PathVariable Integer id) {
        return MessageResponse.setResponse(
                true, HttpStatus.OK, "Vale de consumo obtenido correctamente.",
                valeConsumoService.obtener(id)
        );
    }

    @PostMapping
    public ResponseEntity<Object> crear(@Valid @RequestBody ValeConsumoRequest request) {
        return MessageResponse.setResponse(
                true,
                HttpStatus.CREATED,
                "Vale de consumo registrado correctamente.",
                valeConsumoService.crear(request)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ValeConsumoRequest request
    ) {
        return MessageResponse.setResponse(
                true, HttpStatus.OK, "Vale de consumo actualizado correctamente.",
                valeConsumoService.actualizar(id, request)
        );
    }

    @PatchMapping("/{id}/anular")
    public ResponseEntity<Object> anular(@PathVariable Integer id) {
        return MessageResponse.setResponse(
                true, HttpStatus.OK, "Vale de consumo anulado correctamente.",
                valeConsumoService.anular(id)
        );
    }
}
