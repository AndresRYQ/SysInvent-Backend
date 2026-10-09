package com.agrihusac.SysInvent.service.dashboard.impl;

import com.agrihusac.SysInvent.common.ModuloNoImplementadoException;
import com.agrihusac.SysInvent.service.dashboard.DashboardService;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Override
    public void operacionPendiente() {
        throw new ModuloNoImplementadoException("dashboard");
    }
}
