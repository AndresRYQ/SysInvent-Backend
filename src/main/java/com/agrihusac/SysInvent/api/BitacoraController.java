package com.agrihusac.SysInvent.api;

import com.agrihusac.SysInvent.model.request.BitacoraRequest;
import com.agrihusac.SysInvent.model.projection.BitacoraProjection;
import com.agrihusac.SysInvent.service.bitacora.BitacoraService;
import com.agrihusac.SysInvent.utils.MessageResponse;
import com.agrihusac.SysInvent.utils.CustomPage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/bitacora")
@RequiredArgsConstructor
@Validated
public class BitacoraController {

    private final BitacoraService bitacoraService;

    @GetMapping
    public CustomPage<BitacoraProjection> listar(
            @RequestParam(value = "busqueda", required = false) String busqueda,
            @RequestParam(value = "modulo", required = false) String modulo,
            @RequestParam(value = "accion", required = false) String accion,
            @RequestParam(value = "fechaDesde", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(value = "fechaHasta", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(value = "pagina", defaultValue = "1") @Min(1) Integer pagina,
            @RequestParam(value = "tamPagina", defaultValue = "10") @Min(1) @Max(100) Integer tamPagina) {
        if (fechaDesde != null && fechaHasta != null && fechaDesde.isAfter(fechaHasta)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La fecha desde no puede ser posterior a la fecha hasta");
        }
        Pageable pageable = PageRequest.of(pagina - 1, tamPagina);
        return bitacoraService.listar(busqueda, modulo, accion, fechaDesde, fechaHasta, pageable);
    }

    @PostMapping
    public ResponseEntity<MessageResponse> registrar(@Valid @RequestBody BitacoraRequest request) {
        return bitacoraService.registrar(request);
    }
}
