package com.agrihusac.SysInvent.api.almacen;

import com.agrihusac.SysInvent.service.almacen.ControlAlmacenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Proposed route base: /api/almacen. Movements are derived; this placeholder returns HTTP 501. */
@RestController
@RequestMapping("/api/almacen")
@RequiredArgsConstructor
public class ControlAlmacenController {

    private final ControlAlmacenService controlAlmacenService;

    @GetMapping("/_plantilla")
    public void plantilla() {
        controlAlmacenService.operacionPendiente();
    }
}
