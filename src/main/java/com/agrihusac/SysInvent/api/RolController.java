package com.agrihusac.SysInvent.api;

import com.agrihusac.SysInvent.model.request.ActualizarRolRequest;
import com.agrihusac.SysInvent.model.request.RolRequest;
import com.agrihusac.SysInvent.model.response.RolResponse;
import com.agrihusac.SysInvent.service.RolService;
import com.agrihusac.SysInvent.utils.CustomPage;
import com.agrihusac.SysInvent.utils.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Validated
@Tag(name = "Roles", description = "Operaciones de roles")
public class RolController {

    private final RolService rolService;

    @GetMapping
    @Operation(summary = "Listar roles", description = "Lista los roles registrados con paginación")
    public CustomPage<RolResponse> listarRoles(
            @RequestParam(value = "nombre", required = false) String nombre,
            @Min(value = 1, message = "La página debe ser mayor o igual a 1")
            @RequestParam(value = "pagina", defaultValue = "1") Integer pagina,
            @Min(value = 1, message = "El tamaño de página debe ser mayor o igual a 1")
            @RequestParam(value = "tamPagina", defaultValue = "10") Integer tamanioPagina) {
        Pageable pageable = PageRequest.of(pagina - 1, tamanioPagina);
        return rolService.listarRoles(nombre, pageable);
    }

    @PostMapping
    @Operation(summary = "Registrar rol", description = "Registra un nuevo rol")
    public ResponseEntity<MessageResponse> registrarRol(@Valid @RequestBody RolRequest request) {
        return rolService.registrarRol(request);
    }

    @PutMapping
    @Operation(summary = "Actualizar rol", description = "Actualiza el rol y sus módulos asociados")
    public ResponseEntity<MessageResponse> actualizarRol(@Valid @RequestBody ActualizarRolRequest request) {
        return rolService.actualizarRol(request);
    }

    @DeleteMapping
    @Operation(summary = "Actualizar estado del rol", description = "Activa o desactiva un rol")
    public ResponseEntity<MessageResponse> actualizarEstadoRol(
            @NotNull(message = "{message.required}") @RequestParam("rolId") Integer rolId,
            @NotNull(message = "{message.required}") @RequestParam("activo") Boolean activo) {
        return rolService.actualizarEstadoRol(rolId, activo);
    }
}
