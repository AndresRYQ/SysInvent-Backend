package com.agrihusac.SysInvent.service.ingreso.impl;

import com.agrihusac.SysInvent.common.ModuloNoImplementadoException;
import com.agrihusac.SysInvent.service.ingreso.IngresoAlmacenService;
import org.springframework.stereotype.Service;

@Service
public class IngresoAlmacenServiceImpl implements IngresoAlmacenService {

    @Override
    public void operacionPendiente() {
        throw new ModuloNoImplementadoException("ingresos al almacén");
    }
}
