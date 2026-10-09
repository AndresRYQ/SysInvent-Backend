package com.agrihusac.SysInvent.api.ingreso;

import com.agrihusac.SysInvent.service.ingreso.IngresoAlmacenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Proposed route base: /api/ingresos-almacen. The placeholder returns HTTP 501. */
@RestController
@RequestMapping("/api/ingresos-almacen")
@RequiredArgsConstructor
public class IngresoAlmacenController {

    private final IngresoAlmacenService ingresoAlmacenService;

    @GetMapping("/_plantilla")
    public void plantilla() {
        ingresoAlmacenService.operacionPendiente();
    }
}
