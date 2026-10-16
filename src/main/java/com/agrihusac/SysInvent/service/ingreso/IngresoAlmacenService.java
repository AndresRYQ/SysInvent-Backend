package com.agrihusac.SysInvent.service.ingreso;

import com.agrihusac.SysInvent.model.request.ingreso.IngresoAlmacenRequest;
import com.agrihusac.SysInvent.model.response.ingreso.IngresoAlmacenResponse;

import java.util.List;

public interface IngresoAlmacenService {

    List<IngresoAlmacenResponse> listar();

    IngresoAlmacenResponse crear(IngresoAlmacenRequest request);
}
