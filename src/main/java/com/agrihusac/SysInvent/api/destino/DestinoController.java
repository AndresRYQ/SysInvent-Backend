package com.agrihusac.SysInvent.api.destino;

import com.agrihusac.SysInvent.model.request.destino.DestinoRequest;
import com.agrihusac.SysInvent.model.request.destino.EstadoCatalogoRequest;
import com.agrihusac.SysInvent.model.response.destino.DestinoResponse;
import com.agrihusac.SysInvent.service.destino.DestinoService;
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
@RequestMapping("/api/v1/catalogos/destinos")
@RequiredArgsConstructor
@Validated
@Tag(name = "Destinos", description = "Operaciones de destinos")
public class DestinoController {
    private final DestinoService service;

    @GetMapping
    @Operation(operationId = "listarDestinos", summary = "Listar destinos")
    public CustomPage<DestinoResponse> listarDestinos(@RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "pagina", defaultValue = "1") @Min(1) Integer pagina,
            @RequestParam(value = "tamPagina", defaultValue = "10") @Min(1) @Max(100) Integer tamPagina) {
        Pageable pageable = PageRequest.of(pagina - 1, tamPagina);
        return service.listar(nombre, pageable);
    }

    @PostMapping
    @Operation(operationId = "registrarDestino", summary = "Registrar destino")
    public ResponseEntity<MessageResponse> registrarDestino(@Valid @RequestBody DestinoRequest request) { return service.crear(request); }

    @PutMapping("/{id}")
    @Operation(operationId = "actualizarDestino", summary = "Actualizar destino")
    public ResponseEntity<MessageResponse> actualizarDestino(@PathVariable @Min(1) Integer id, @Valid @RequestBody DestinoRequest request) {
        return service.actualizar(id, request);
    }

    @PutMapping("/{id}/estado")
    @Operation(operationId = "cambiarEstadoDestino", summary = "Cambiar estado del destino")
    public ResponseEntity<MessageResponse> cambiarEstadoDestino(@PathVariable @Min(1) Integer id,
            @Valid @RequestBody EstadoCatalogoRequest request) { return service.cambiarEstado(id, request.activo()); }
}
