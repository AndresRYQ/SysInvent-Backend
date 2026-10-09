package com.agrihusac.SysInvent.api;

import com.agrihusac.SysInvent.model.request.catalogo.CategoriaCatalogoRequest;
import com.agrihusac.SysInvent.model.request.catalogo.EstadoCatalogoRequest;
import com.agrihusac.SysInvent.model.response.catalogo.CategoriaCatalogoResponse;
import com.agrihusac.SysInvent.service.CategoriaService;
import com.agrihusac.SysInvent.utils.CustomPage;
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
public class CategoriaCatalogoController {

    private final CategoriaService service;

    @GetMapping
    public CustomPage<CategoriaCatalogoResponse> listar(
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "pagina", defaultValue = "1") @Min(1) Integer pagina,
            @RequestParam(value = "tamPagina", defaultValue = "10") @Min(1) @Max(100) Integer tamPagina) {
        Pageable pageable = PageRequest.of(pagina - 1, tamPagina);
        return service.listar(nombre, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obtener(@PathVariable @Min(1) Integer id) {
        return service.obtener(id);
    }

    @PostMapping
    public ResponseEntity<Object> crear(@Valid @RequestBody CategoriaCatalogoRequest request) {
        return service.crear(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizar(@PathVariable @Min(1) Integer id,
                                             @Valid @RequestBody CategoriaCatalogoRequest request) {
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> desactivar(@PathVariable @Min(1) Integer id) {
        return service.cambiarEstado(id, false);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Object> cambiarEstado(@PathVariable @Min(1) Integer id,
                                                @Valid @RequestBody EstadoCatalogoRequest request) {
        return service.cambiarEstado(id, request.activo());
    }
}
