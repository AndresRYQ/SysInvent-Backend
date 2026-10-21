package com.agrihusac.SysInvent.service.almacen;

import com.agrihusac.SysInvent.model.response.almacen.ControlAlmacenListadoResponse;
import com.agrihusac.SysInvent.model.response.almacen.ControlAlmacenMovimientoResponse;
import com.agrihusac.SysInvent.utils.CustomPage;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ControlAlmacenService {

    CustomPage<ControlAlmacenMovimientoResponse> detalle(Integer productoId, Pageable pageable);

    CustomPage<ControlAlmacenListadoResponse> listar(String busqueda, Integer tipoProductoId,
                                                      Integer categoriaId, String estado,
                                                      LocalDate fechaDesde, LocalDate fechaHasta,
                                                      Pageable pageable);
}
