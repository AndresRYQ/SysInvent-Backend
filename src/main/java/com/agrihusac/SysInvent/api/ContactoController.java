package com.agrihusac.SysInvent.api;

import com.agrihusac.SysInvent.model.request.contacto.ContactoRequest;
import com.agrihusac.SysInvent.model.request.contacto.EstadoCatalogoRequest;
import com.agrihusac.SysInvent.model.response.contacto.ContactoResponse;
import com.agrihusac.SysInvent.service.contacto.ContactoService;
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
@RequestMapping("/api/v1/catalogos/contactos")
@RequiredArgsConstructor
@Validated
@Tag(name = "Contactos", description = "Operaciones de contactos")
public class ContactoController {
    private final ContactoService service;

    @GetMapping
    @Operation(operationId = "listarContactos", summary = "Listar contactos")
    public CustomPage<ContactoResponse> listarContactos(@RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "proveedorId", required = false) Integer proveedorId,
            @RequestParam(value = "pagina", defaultValue = "1") @Min(1) Integer pagina,
            @RequestParam(value = "tamPagina", defaultValue = "10") @Min(1) @Max(100) Integer tamPagina) {
        Pageable pageable = PageRequest.of(pagina - 1, tamPagina);
        if (proveedorId != null) {
            return service.listarPorProveedor(nombre, proveedorId, pageable);
        }
        return service.listar(nombre, pageable);
    }

    @PostMapping
    @Operation(operationId = "registrarContacto", summary = "Registrar contacto")
    public ResponseEntity<MessageResponse> registrarContacto(@Valid @RequestBody ContactoRequest request) { return service.crear(request); }

    @PutMapping("/{id}")
    @Operation(operationId = "actualizarContacto", summary = "Actualizar contacto")
    public ResponseEntity<MessageResponse> actualizarContacto(@PathVariable @Min(1) Integer id, @Valid @RequestBody ContactoRequest request) {
        return service.actualizar(id, request);
    }

    @PutMapping("/{id}/estado")
    @Operation(operationId = "cambiarEstadoContacto", summary = "Cambiar estado del contacto")
    public ResponseEntity<MessageResponse> cambiarEstadoContacto(@PathVariable @Min(1) Integer id,
            @Valid @RequestBody EstadoCatalogoRequest request) { return service.cambiarEstado(id, request.activo()); }
}
