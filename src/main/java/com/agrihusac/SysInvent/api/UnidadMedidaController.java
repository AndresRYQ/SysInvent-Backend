package com.agrihusac.SysInvent.api;

import com.agrihusac.SysInvent.model.request.unidadmedida.UnidadMedidaRequest;
import com.agrihusac.SysInvent.model.request.unidadmedida.EstadoCatalogoRequest;
import com.agrihusac.SysInvent.model.response.unidadmedida.UnidadMedidaResponse;
import com.agrihusac.SysInvent.service.unidadmedida.UnidadMedidaService;
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
@RequestMapping("/api/v1/catalogos/unidades-medida")
@RequiredArgsConstructor
@Validated
@Tag(name = "Unidades de medida", description = "Operaciones de unidades de medida")
public class UnidadMedidaController {
    private final UnidadMedidaService service;

    @GetMapping
    @Operation(operationId = "listarUnidadesMedida", summary = "Listar unidades de medida")
    public CustomPage<UnidadMedidaResponse> listarUnidadesMedida(@RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "pagina", defaultValue = "1") @Min(1) Integer pagina,
            @RequestParam(value = "tamPagina", defaultValue = "10") @Min(1) @Max(100) Integer tamPagina) {
        Pageable pageable = PageRequest.of(pagina - 1, tamPagina);
        return service.listar(nombre, pageable);
    }

    @PostMapping
    @Operation(operationId = "registrarUnidadMedida", summary = "Registrar unidad de medida")
    public ResponseEntity<MessageResponse> registrarUnidadMedida(@Valid @RequestBody UnidadMedidaRequest request) { return service.crear(request); }

    @PutMapping("/{id}")
    @Operation(operationId = "actualizarUnidadMedida", summary = "Actualizar unidad de medida")
    public ResponseEntity<MessageResponse> actualizarUnidadMedida(@PathVariable @Min(1) Integer id, @Valid @RequestBody UnidadMedidaRequest request) {
        return service.actualizar(id, request);
    }

    @PutMapping("/{id}/estado")
    @Operation(operationId = "cambiarEstadoUnidadMedida", summary = "Cambiar estado de unidad de medida")
    public ResponseEntity<MessageResponse> cambiarEstadoUnidadMedida(@PathVariable @Min(1) Integer id,
            @Valid @RequestBody EstadoCatalogoRequest request) { return service.cambiarEstado(id, request.activo()); }
}
