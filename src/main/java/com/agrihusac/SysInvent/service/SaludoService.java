package com.agrihusac.SysInvent.service;

import com.agrihusac.SysInvent.model.request.SaludoRequest;
import com.agrihusac.SysInvent.model.response.SaludoResponse;

public interface SaludoService {
    SaludoResponse crearSaludo(SaludoRequest request);
}
