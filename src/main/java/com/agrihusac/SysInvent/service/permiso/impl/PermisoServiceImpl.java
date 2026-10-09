package com.agrihusac.SysInvent.service.permiso.impl;

import com.agrihusac.SysInvent.common.ModuloNoImplementadoException;
import com.agrihusac.SysInvent.service.permiso.PermisoService;
import org.springframework.stereotype.Service;

@Service
public class PermisoServiceImpl implements PermisoService {

    @Override
    public void operacionPendiente() {
        throw new ModuloNoImplementadoException("roles y permisos");
    }
}
