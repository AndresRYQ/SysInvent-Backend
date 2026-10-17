package com.agrihusac.SysInvent.api;

import com.agrihusac.SysInvent.model.request.centrocosto.CentroCostoRequest;
import com.agrihusac.SysInvent.model.request.centrocosto.EstadoCatalogoRequest;
import com.agrihusac.SysInvent.model.response.centrocosto.CentroCostoResponse;
import com.agrihusac.SysInvent.service.centrocosto.CentroCostoService;
import com.agrihusac.SysInvent.utils.CustomPage;
import com.agrihusac.SysInvent.utils.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/catalogos/centros-costo")
@RequiredArgsConstructor
@Validated
@Tag(name = "Centros de costo", description = "Operaciones de centros de costo")
public class CentroCostoController {
    private final CentroCostoService service;

    @GetMapping
    @Operation(operationId = "listarCentrosCosto", summary = "Listar centros de costo")
    public CustomPage<CentroCostoResponse> listarCentrosCosto(@RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "pagina", defaultValue = "1") @Min(1) Integer pagina,
            @RequestParam(value = "tamPagina", defaultValue = "10") @Min(1) @Max(100) Integer tamPagina) {
        Pageable pageable = PageRequest.of(pagina - 1, tamPagina);
        return service.listar(nombre, pageable);
    }

    @PostMapping
    @Operation(operationId = "registrarCentroCosto", summary = "Registrar centro de costo")
    public ResponseEntity<MessageResponse> registrarCentroCosto(@Valid @RequestBody CentroCostoRequest request) { return service.crear(request); }

    @PutMapping("/{id}")
    @Operation(operationId = "actualizarCentroCosto", summary = "Actualizar centro de costo")
    public ResponseEntity<MessageResponse> actualizarCentroCosto(@PathVariable @Min(1) Integer id, @Valid @RequestBody CentroCostoRequest request) {
        return service.actualizar(id, request);
    }

    @PutMapping("/{id}/estado")
    @Operation(operationId = "cambiarEstadoCentroCosto", summary = "Cambiar estado del centro de costo")
    public ResponseEntity<MessageResponse> cambiarEstadoCentroCosto(@PathVariable @Min(1) Integer id,
            @Valid @RequestBody EstadoCatalogoRequest request) { return service.cambiarEstado(id, request.activo()); }
}
