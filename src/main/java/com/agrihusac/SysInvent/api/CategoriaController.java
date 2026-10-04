package com.agrihusac.SysInvent.api;

import com.agrihusac.SysInvent.model.request.CategoriaRequest;
import com.agrihusac.SysInvent.model.response.CategoriaResponse;
import com.agrihusac.SysInvent.service.CategoriaService;
import com.agrihusac.SysInvent.utils.CustomPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
@Validated
@Tag(name = "Categorías", description = "Operaciones de categorías")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    @Operation(summary = "Listar categorías", description = "Lista las categorías activas con paginación")
    public CustomPage<CategoriaResponse> listarCategorias(
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "pagina", defaultValue = "1") Integer pagina,
            @RequestParam(value = "tamPagina", defaultValue = "10") Integer tamanioPagina) {
        Pageable pageable = PageRequest.of(pagina - 1, tamanioPagina);
        return categoriaService.listarCategorias(nombre, pageable);
    }

    @PostMapping
    @Operation(summary = "Registrar o actualizar categoría",
            description = "Registra cuando categoriaId es 0 y actualiza cuando es mayor que 0")
    public ResponseEntity<Object> registrarActualizarCategoria(
            @Valid @RequestBody CategoriaRequest request) {
        return categoriaService.registrarActualizarCategoria(request);
    }

    @DeleteMapping
    @Operation(summary = "Eliminar categoría", description = "Desactiva lógicamente una categoría")
    public ResponseEntity<Object> eliminarCategoria(
            @NotNull(message = "{message.required}") @RequestParam("categoriaId") Integer categoriaId) {
        return categoriaService.eliminarCategoria(categoriaId);
    }
}
