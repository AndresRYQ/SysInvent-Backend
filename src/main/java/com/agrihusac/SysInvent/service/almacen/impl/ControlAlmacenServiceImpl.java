package com.agrihusac.SysInvent.service.almacen.impl;

import com.agrihusac.SysInvent.common.ModuloNoImplementadoException;
import com.agrihusac.SysInvent.service.almacen.ControlAlmacenService;
import org.springframework.stereotype.Service;

@Service
public class ControlAlmacenServiceImpl implements ControlAlmacenService {

    @Override
    public void operacionPendiente() {
        throw new ModuloNoImplementadoException("control de almacén y Kardex");
    }
}
