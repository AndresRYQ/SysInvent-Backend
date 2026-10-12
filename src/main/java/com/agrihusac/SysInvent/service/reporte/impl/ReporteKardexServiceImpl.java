package com.agrihusac.SysInvent.service.reporte.impl;

import com.agrihusac.SysInvent.common.ModuloNoImplementadoException;
import com.agrihusac.SysInvent.service.reporte.ReporteKardexService;
import org.springframework.stereotype.Service;

@Service
public class ReporteKardexServiceImpl implements ReporteKardexService {

    @Override
    public void operacionPendiente() {
        throw new ModuloNoImplementadoException("reporte de Kardex");
    }
}
