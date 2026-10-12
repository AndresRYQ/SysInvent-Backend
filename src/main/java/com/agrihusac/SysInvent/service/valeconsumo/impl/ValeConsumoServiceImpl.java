package com.agrihusac.SysInvent.service.valeconsumo.impl;

import com.agrihusac.SysInvent.common.ModuloNoImplementadoException;
import com.agrihusac.SysInvent.service.valeconsumo.ValeConsumoService;
import org.springframework.stereotype.Service;

@Service
public class ValeConsumoServiceImpl implements ValeConsumoService {

    @Override
    public void operacionPendiente() {
        throw new ModuloNoImplementadoException("vales de consumo");
    }
}
