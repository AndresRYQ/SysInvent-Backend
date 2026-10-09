package com.agrihusac.SysInvent.service.reporte.impl;

import com.agrihusac.SysInvent.common.ModuloNoImplementadoException;
import com.agrihusac.SysInvent.service.reporte.ReporteProductoMasPedidoService;
import org.springframework.stereotype.Service;

@Service
public class ReporteProductoMasPedidoServiceImpl implements ReporteProductoMasPedidoService {

    @Override
    public void operacionPendiente() {
        throw new ModuloNoImplementadoException("reporte de productos más pedidos");
    }
}
