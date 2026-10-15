package com.agrihusac.SysInvent.service.producto.impl;

import com.agrihusac.SysInvent.common.ModuloNoImplementadoException;
import com.agrihusac.SysInvent.service.producto.ProductoService;
import org.springframework.stereotype.Service;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Override
    public void operacionPendiente() {
        throw new ModuloNoImplementadoException("productos");
    }
}
