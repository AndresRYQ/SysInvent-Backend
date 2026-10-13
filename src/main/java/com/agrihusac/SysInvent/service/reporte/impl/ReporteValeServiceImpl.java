package com.agrihusac.SysInvent.service.reporte.impl;

import com.agrihusac.SysInvent.common.ModuloNoImplementadoException;
import com.agrihusac.SysInvent.service.reporte.ReporteValeService;
import org.springframework.stereotype.Service;

@Service
public class ReporteValeServiceImpl implements ReporteValeService {

    @Override
    public void operacionPendiente() {
        throw new ModuloNoImplementadoException("reporte de vales");
    }
}
