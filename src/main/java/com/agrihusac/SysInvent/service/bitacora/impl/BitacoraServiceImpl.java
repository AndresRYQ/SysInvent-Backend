package com.agrihusac.SysInvent.service.bitacora.impl;

import com.agrihusac.SysInvent.common.ModuloNoImplementadoException;
import com.agrihusac.SysInvent.service.bitacora.BitacoraService;
import org.springframework.stereotype.Service;

@Service
public class BitacoraServiceImpl implements BitacoraService {

    @Override
    public void operacionPendiente() {
        throw new ModuloNoImplementadoException("bitácora");
    }
}
