package com.agrihusac.SysInvent.api.valeconsumo;

import com.agrihusac.SysInvent.model.request.valeconsumo.ValeConsumoRequest;
import com.agrihusac.SysInvent.service.valeconsumo.ValeConsumoService;
import com.agrihusac.SysInvent.utils.MessageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vales-consumo")
@RequiredArgsConstructor
public class ValeConsumoController {

    private final ValeConsumoService valeConsumoService;

    @GetMapping
    public ResponseEntity<Object> listar() {
        return MessageResponse.setResponse(
                true, HttpStatus.OK, "Vales de consumo obtenidos correctamente.",
                valeConsumoService.listar()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obtener(@PathVariable Integer id) {
        return MessageResponse.setResponse(
                true, HttpStatus.OK, "Vale de consumo obtenido correctamente.",
                valeConsumoService.obtener(id)
        );
    }

    @PostMapping
    public ResponseEntity<Object> crear(@Valid @RequestBody ValeConsumoRequest request) {
        return MessageResponse.setResponse(
                true, HttpStatus.CREATED, "Vale de consumo registrado correctamente.",
                valeConsumoService.crear(request)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ValeConsumoRequest request
    ) {
        return MessageResponse.setResponse(
                true, HttpStatus.OK, "Vale de consumo actualizado correctamente.",
                valeConsumoService.actualizar(id, request)
        );
    }

    @PatchMapping("/{id}/anular")
    public ResponseEntity<Object> anular(@PathVariable Integer id) {
        return MessageResponse.setResponse(
                true, HttpStatus.OK, "Vale de consumo anulado correctamente.",
                valeConsumoService.anular(id)
        );
    }
}
