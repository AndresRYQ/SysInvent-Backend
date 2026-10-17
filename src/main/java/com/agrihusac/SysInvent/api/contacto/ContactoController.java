package com.agrihusac.SysInvent.api.contacto;

import com.agrihusac.SysInvent.model.request.contacto.ContactoRequest;
import com.agrihusac.SysInvent.model.request.contacto.EstadoCatalogoRequest;
import com.agrihusac.SysInvent.model.response.contacto.ContactoResponse;
import com.agrihusac.SysInvent.service.contacto.ContactoService;
import com.agrihusac.SysInvent.utils.CustomPage;
import com.agrihusac.SysInvent.utils.MessageResponse;
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
public class ContactoController {
    private final ContactoService service;

    @GetMapping
    public CustomPage<ContactoResponse> listar(@RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "pagina", defaultValue = "1") @Min(1) Integer pagina,
            @RequestParam(value = "tamPagina", defaultValue = "10") @Min(1) @Max(100) Integer tamPagina) {
        Pageable pageable = PageRequest.of(pagina - 1, tamPagina);
        return service.listar(nombre, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageResponse> obtener(@PathVariable @Min(1) Integer id) { return service.obtener(id); }

    @PostMapping
    public ResponseEntity<MessageResponse> crear(@Valid @RequestBody ContactoRequest request) { return service.crear(request); }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> actualizar(@PathVariable @Min(1) Integer id, @Valid @RequestBody ContactoRequest request) {
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> desactivar(@PathVariable @Min(1) Integer id) { return service.cambiarEstado(id, false); }

    @PutMapping("/{id}/estado")
    public ResponseEntity<MessageResponse> cambiarEstado(@PathVariable @Min(1) Integer id,
            @Valid @RequestBody EstadoCatalogoRequest request) { return service.cambiarEstado(id, request.activo()); }
}
