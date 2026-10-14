package com.agrihusac.SysInvent.service.valeconsumo;

import com.agrihusac.SysInvent.model.request.valeconsumo.ValeConsumoRequest;
import com.agrihusac.SysInvent.model.response.valeconsumo.ValeConsumoResponse;

import java.util.List;

public interface ValeConsumoService {
    List<ValeConsumoResponse> listar();

    ValeConsumoResponse obtener(Integer id);

    ValeConsumoResponse crear(ValeConsumoRequest request);

    ValeConsumoResponse actualizar(Integer id, ValeConsumoRequest request);

    ValeConsumoResponse anular(Integer id);
}
