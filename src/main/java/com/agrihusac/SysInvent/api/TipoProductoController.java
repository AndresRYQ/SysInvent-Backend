package com.agrihusac.SysInvent.api;

import com.agrihusac.SysInvent.model.request.tipoproducto.TipoProductoRequest;
import com.agrihusac.SysInvent.model.request.tipoproducto.EstadoCatalogoRequest;
import com.agrihusac.SysInvent.model.response.tipoproducto.TipoProductoResponse;
import com.agrihusac.SysInvent.service.tipoproducto.TipoProductoService;
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
@RequestMapping("/api/v1/catalogos/tipos-producto")
@RequiredArgsConstructor
@Validated
@Tag(name = "Tipos de producto", description = "Operaciones de tipos de producto")
public class TipoProductoController {
    private final TipoProductoService service;

    @GetMapping
    @Operation(operationId = "listarTiposProducto", summary = "Listar tipos de producto")
    public CustomPage<TipoProductoResponse> listarTiposProducto(@RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "pagina", defaultValue = "1") @Min(1) Integer pagina,
            @RequestParam(value = "tamPagina", defaultValue = "10") @Min(1) @Max(100) Integer tamPagina) {
        Pageable pageable = PageRequest.of(pagina - 1, tamPagina);
        return service.listar(nombre, pageable);
    }

    @PostMapping
    @Operation(operationId = "registrarTipoProducto", summary = "Registrar tipo de producto")
    public ResponseEntity<MessageResponse> registrarTipoProducto(@Valid @RequestBody TipoProductoRequest request) { return service.crear(request); }

    @PutMapping("/{id}")
    @Operation(operationId = "actualizarTipoProducto", summary = "Actualizar tipo de producto")
    public ResponseEntity<MessageResponse> actualizarTipoProducto(@PathVariable @Min(1) Integer id, @Valid @RequestBody TipoProductoRequest request) {
        return service.actualizar(id, request);
    }

    @PutMapping("/{id}/estado")
    @Operation(operationId = "cambiarEstadoTipoProducto", summary = "Cambiar estado del tipo de producto")
    public ResponseEntity<MessageResponse> cambiarEstadoTipoProducto(@PathVariable @Min(1) Integer id,
            @Valid @RequestBody EstadoCatalogoRequest request) { return service.cambiarEstado(id, request.activo()); }
}
