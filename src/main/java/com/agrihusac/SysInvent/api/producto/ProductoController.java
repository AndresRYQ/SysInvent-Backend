package com.agrihusac.SysInvent.api.producto;

import com.agrihusac.SysInvent.service.producto.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Proposed route base: /api/productos. The placeholder returns HTTP 501. */
@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping("/_plantilla")
    public void plantilla() {
        productoService.operacionPendiente();
    }
}
