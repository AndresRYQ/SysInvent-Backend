package com.agrihusac.SysInvent.api;

import com.agrihusac.SysInvent.model.request.parteequipo.ParteEquipoRequest;
import com.agrihusac.SysInvent.model.request.parteequipo.EstadoCatalogoRequest;
import com.agrihusac.SysInvent.model.response.parteequipo.ParteEquipoResponse;
import com.agrihusac.SysInvent.service.parteequipo.ParteEquipoService;
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
@RequestMapping("/api/v1/catalogos/partes-equipo")
@RequiredArgsConstructor
@Validated
@Tag(name = "Partes de equipo", description = "Operaciones de partes de equipo")
public class ParteEquipoController {
    private final ParteEquipoService service;

    @GetMapping
    @Operation(operationId = "listarPartesEquipo", summary = "Listar partes de equipo")
    public CustomPage<ParteEquipoResponse> listarPartesEquipo(@RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "pagina", defaultValue = "1") @Min(1) Integer pagina,
            @RequestParam(value = "tamPagina", defaultValue = "10") @Min(1) @Max(100) Integer tamPagina) {
        Pageable pageable = PageRequest.of(pagina - 1, tamPagina);
        return service.listar(nombre, pageable);
    }

    @PostMapping
    @Operation(operationId = "registrarParteEquipo", summary = "Registrar parte de equipo")
    public ResponseEntity<MessageResponse> registrarParteEquipo(@Valid @RequestBody ParteEquipoRequest request) { return service.crear(request); }

    @PutMapping("/{id}")
    @Operation(operationId = "actualizarParteEquipo", summary = "Actualizar parte de equipo")
    public ResponseEntity<MessageResponse> actualizarParteEquipo(@PathVariable @Min(1) Integer id, @Valid @RequestBody ParteEquipoRequest request) {
        return service.actualizar(id, request);
    }

    @PutMapping("/{id}/estado")
    @Operation(operationId = "cambiarEstadoParteEquipo", summary = "Cambiar estado de la parte de equipo")
    public ResponseEntity<MessageResponse> cambiarEstadoParteEquipo(@PathVariable @Min(1) Integer id,
            @Valid @RequestBody EstadoCatalogoRequest request) { return service.cambiarEstado(id, request.activo()); }
}
