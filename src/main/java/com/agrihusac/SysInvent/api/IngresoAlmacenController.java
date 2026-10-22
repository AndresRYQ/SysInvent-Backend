package com.agrihusac.SysInvent.api;

import com.agrihusac.SysInvent.model.request.ingreso.IngresoAlmacenRequest;
import com.agrihusac.SysInvent.model.response.ingreso.IngresoAlmacenListadoResponse;
import com.agrihusac.SysInvent.service.ingreso.IngresoAlmacenService;
import com.agrihusac.SysInvent.utils.CustomPage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/ingresos-almacen")
@RequiredArgsConstructor
@Validated
public class IngresoAlmacenController {

    private final IngresoAlmacenService ingresoAlmacenService;

    @GetMapping
    public CustomPage<IngresoAlmacenListadoResponse> listar(
            @RequestParam(value = "busqueda", required = false) String busqueda,
            @RequestParam(value = "proveedorId", required = false) @Min(1) Integer proveedorId,
            @RequestParam(value = "estado", required = false) String estado,
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
        return ingresoAlmacenService.listar(busqueda, proveedorId, estado, fechaDesde, fechaHasta, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obtener(@PathVariable @Min(1) Integer id) {
        return ingresoAlmacenService.obtener(id);
    }

    @PostMapping
    public ResponseEntity<Object> crear(@Valid @RequestBody IngresoAlmacenRequest request) {
        return ingresoAlmacenService.registrar(request);
    }
}
