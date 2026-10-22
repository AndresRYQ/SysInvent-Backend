package com.agrihusac.SysInvent.service.valeconsumo;

import com.agrihusac.SysInvent.model.request.valeconsumo.ValeConsumoRequest;
import com.agrihusac.SysInvent.model.response.valeconsumo.ValeConsumoResponse;
import com.agrihusac.SysInvent.model.response.valeconsumo.ValeConsumoListadoResponse;
import com.agrihusac.SysInvent.utils.CustomPage;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ValeConsumoService {
    CustomPage<ValeConsumoListadoResponse> listar(String busqueda, Integer centroCostoId,
                                                   String estado, LocalDate fechaDesde,
                                                   LocalDate fechaHasta, Pageable pageable);

    ValeConsumoResponse obtener(Integer id);

    ValeConsumoResponse crear(ValeConsumoRequest request);

    ValeConsumoResponse actualizar(Integer id, ValeConsumoRequest request);

    ValeConsumoResponse anular(Integer id);
}
