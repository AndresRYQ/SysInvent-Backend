package com.agrihusac.SysInvent.service.perfil.impl;

import com.agrihusac.SysInvent.common.ModuloNoImplementadoException;
import com.agrihusac.SysInvent.service.perfil.PerfilService;
import org.springframework.stereotype.Service;

@Service
public class PerfilServiceImpl implements PerfilService {

    @Override
    public void operacionPendiente() {
        throw new ModuloNoImplementadoException("perfil de usuario");
    }
}
