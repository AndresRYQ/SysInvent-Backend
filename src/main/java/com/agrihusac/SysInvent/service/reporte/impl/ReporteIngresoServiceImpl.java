package com.agrihusac.SysInvent.service.reporte.impl;

import com.agrihusac.SysInvent.common.ModuloNoImplementadoException;
import com.agrihusac.SysInvent.service.reporte.ReporteIngresoService;
import org.springframework.stereotype.Service;

@Service
public class ReporteIngresoServiceImpl implements ReporteIngresoService {

    @Override
    public void operacionPendiente() {
        throw new ModuloNoImplementadoException("reporte de ingresos");
    }
}
