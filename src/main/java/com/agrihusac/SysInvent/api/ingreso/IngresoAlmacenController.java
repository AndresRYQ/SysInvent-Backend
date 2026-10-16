package com.agrihusac.SysInvent.api.ingreso;

import com.agrihusac.SysInvent.service.ingreso.IngresoAlmacenService;
import com.agrihusac.SysInvent.model.request.ingreso.IngresoAlmacenRequest;
import com.agrihusac.SysInvent.utils.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ingresos-almacen")
@RequiredArgsConstructor
@Tag(name = "Ingresos de almacén", description = "Registro de entradas de inventario")
public class IngresoAlmacenController {

    private final IngresoAlmacenService ingresoAlmacenService;

    @GetMapping
    @Operation(summary = "Listar ingresos de almacén")
    public ResponseEntity<Object> listar() {
        return MessageResponse.setResponse(true, HttpStatus.OK,
                "Ingresos de almacén obtenidos correctamente.", ingresoAlmacenService.listar());
    }

    @PostMapping
    @Operation(summary = "Registrar un ingreso y actualizar existencias")
    public ResponseEntity<Object> crear(@Valid @RequestBody IngresoAlmacenRequest request) {
        return MessageResponse.setResponse(true, HttpStatus.CREATED,
                "Ingreso de almacén registrado correctamente.", ingresoAlmacenService.crear(request));
    }
}
