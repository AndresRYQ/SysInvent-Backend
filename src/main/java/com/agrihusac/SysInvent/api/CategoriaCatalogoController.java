package com.agrihusac.SysInvent.api;

import com.agrihusac.SysInvent.model.request.catalogo.CategoriaCatalogoRequest;
import com.agrihusac.SysInvent.model.request.catalogo.EstadoCatalogoRequest;
import com.agrihusac.SysInvent.model.response.catalogo.CategoriaCatalogoResponse;
import com.agrihusac.SysInvent.service.CategoriaService;
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
@RequestMapping("/api/v1/catalogos/categorias")
@RequiredArgsConstructor
@Validated
@Tag(name = "Categorías", description = "Operaciones de categorías")
public class CategoriaCatalogoController {

    private final CategoriaService service;

    @GetMapping
    @Operation(operationId = "listarCategoriasCatalogo", summary = "Listar categorías")
    public CustomPage<CategoriaCatalogoResponse> listarCategorias(
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "pagina", defaultValue = "1") @Min(1) Integer pagina,
            @RequestParam(value = "tamPagina", defaultValue = "10") @Min(1) @Max(100) Integer tamPagina) {
        Pageable pageable = PageRequest.of(pagina - 1, tamPagina);
        return service.listar(nombre, pageable);
    }

    @PostMapping
    @Operation(operationId = "registrarCategoria", summary = "Registrar categoría")
    public ResponseEntity<MessageResponse> registrarCategoria(@Valid @RequestBody CategoriaCatalogoRequest request) {
        return service.crear(request);
    }

    @PutMapping("/{id}")
    @Operation(operationId = "actualizarCategoria", summary = "Actualizar categoría")
    public ResponseEntity<MessageResponse> actualizarCategoria(@PathVariable @Min(1) Integer id,
                                             @Valid @RequestBody CategoriaCatalogoRequest request) {
        return service.actualizar(id, request);
    }

    @PutMapping("/{id}/estado")
    @Operation(operationId = "cambiarEstadoCategoria", summary = "Cambiar estado de categoría")
    public ResponseEntity<MessageResponse> cambiarEstadoCategoria(@PathVariable @Min(1) Integer id,
                                                @Valid @RequestBody EstadoCatalogoRequest request) {
        return service.cambiarEstado(id, request.activo());
    }
}
