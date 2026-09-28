package com.agrihusac.SysInvent.api;

import com.agrihusac.SysInvent.model.request.RolRequest;
import com.agrihusac.SysInvent.model.response.RolResponse;
import com.agrihusac.SysInvent.service.RolService;
import com.agrihusac.SysInvent.utils.CustomPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Tag(name = "Roles", description = "Operaciones de roles")
public class RolController {

    private final RolService rolService;

    @GetMapping
    @Operation(summary = "Listar roles", description = "Lista los roles registrados con paginación")
    public CustomPage<RolResponse> listarRoles(
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "pagina", defaultValue = "1") Integer pagina,
            @RequestParam(value = "tamPagina", defaultValue = "10") Integer tamanioPagina) {
        Pageable pageable = PageRequest.of(pagina - 1, tamanioPagina);
        return rolService.listarRoles(nombre, pageable);
    }

    @PostMapping
    @Operation(summary = "Registrar rol", description = "Registra un nuevo rol")
    public ResponseEntity<Object> registrarRol(@Valid @RequestBody RolRequest request) {
        return rolService.registrarRol(request);
    }
}
