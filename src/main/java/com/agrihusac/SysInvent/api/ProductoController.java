package com.agrihusac.SysInvent.api;

import com.agrihusac.SysInvent.model.request.catalogo.EstadoCatalogoRequest;
import com.agrihusac.SysInvent.model.request.producto.ProductoRequest;
import com.agrihusac.SysInvent.model.response.producto.ProductoResponse;
import com.agrihusac.SysInvent.service.producto.ProductoService;
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
@RequestMapping("/api/v1/catalogos/productos")
@RequiredArgsConstructor
@Validated
@Tag(name = "Productos", description = "Operaciones de productos")
public class ProductoController {
    private final ProductoService service;

    @GetMapping
    @Operation(operationId = "listarProductos", summary = "Listar productos")
    public CustomPage<ProductoResponse> listarProductos(@RequestParam(value = "nombre", required = false) String nombre,
                                                       @RequestParam(value = "pagina", defaultValue = "1") @Min(1) Integer pagina,
                                                       @RequestParam(value = "tamPagina", defaultValue = "10") @Min(1) @Max(100) Integer tamPagina) {
        Pageable pageable = PageRequest.of(pagina - 1, tamPagina);
        return service.listar(nombre, pageable);
    }

    @GetMapping("/{id}")
    @Operation(operationId = "obtenerProducto", summary = "Obtener producto")
    public ResponseEntity<MessageResponse> obtenerProducto(@PathVariable @Min(1) Integer id) {
        return service.obtener(id);
    }

    @PostMapping
    @Operation(operationId = "registrarProducto", summary = "Registrar producto")
    public ResponseEntity<MessageResponse> registrarProducto(@Valid @RequestBody ProductoRequest request) {
        return service.crear(request);
    }

    @PutMapping("/{id}")
    @Operation(operationId = "actualizarProducto", summary = "Actualizar producto")
    public ResponseEntity<MessageResponse> actualizarProducto(@PathVariable @Min(1) Integer id, @Valid @RequestBody ProductoRequest request) {
        return service.actualizar(id, request);
    }

    @PutMapping("/{id}/estado")
    @Operation(operationId = "cambiarEstadoProducto", summary = "Cambiar estado del producto")
    public ResponseEntity<MessageResponse> cambiarEstadoProducto(@PathVariable @Min(1) Integer id,
                                                                 @Valid @RequestBody EstadoCatalogoRequest request) {
        return service.cambiarEstado(id, request.activo());
    }

    @DeleteMapping("/{id}")
    @Operation(operationId = "eliminarProducto", summary = "Desactivar producto")
    public ResponseEntity<MessageResponse> eliminarProducto(@PathVariable @Min(1) Integer id) {
        return service.cambiarEstado(id, false);
    }
}
