package com.agrihusac.SysInvent.api;

import com.agrihusac.SysInvent.model.request.tipodocumento.TipoDocumentoRequest;
import com.agrihusac.SysInvent.model.request.tipodocumento.EstadoCatalogoRequest;
import com.agrihusac.SysInvent.model.response.tipodocumento.TipoDocumentoResponse;
import com.agrihusac.SysInvent.service.tipodocumento.TipoDocumentoService;
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
@RequestMapping("/api/v1/catalogos/tipos-documento")
@RequiredArgsConstructor
@Validated
@Tag(name = "Tipos de documento", description = "Operaciones de tipos de documento")
public class TipoDocumentoController {
    private final TipoDocumentoService service;

    @GetMapping
    @Operation(operationId = "listarTiposDocumento", summary = "Listar tipos de documento")
    public CustomPage<TipoDocumentoResponse> listarTiposDocumento(@RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "pagina", defaultValue = "1") @Min(1) Integer pagina,
            @RequestParam(value = "tamPagina", defaultValue = "10") @Min(1) @Max(100) Integer tamPagina) {
        Pageable pageable = PageRequest.of(pagina - 1, tamPagina);
        return service.listar(nombre, pageable);
    }

    @PostMapping
    @Operation(operationId = "registrarTipoDocumento", summary = "Registrar tipo de documento")
    public ResponseEntity<MessageResponse> registrarTipoDocumento(@Valid @RequestBody TipoDocumentoRequest request) { return service.crear(request); }

    @PutMapping("/{id}")
    @Operation(operationId = "actualizarTipoDocumento", summary = "Actualizar tipo de documento")
    public ResponseEntity<MessageResponse> actualizarTipoDocumento(@PathVariable @Min(1) Integer id, @Valid @RequestBody TipoDocumentoRequest request) {
        return service.actualizar(id, request);
    }

    @PutMapping("/{id}/estado")
    @Operation(operationId = "cambiarEstadoTipoDocumento", summary = "Cambiar estado del tipo de documento")
    public ResponseEntity<MessageResponse> cambiarEstadoTipoDocumento(@PathVariable @Min(1) Integer id,
            @Valid @RequestBody EstadoCatalogoRequest request) { return service.cambiarEstado(id, request.activo()); }
}
