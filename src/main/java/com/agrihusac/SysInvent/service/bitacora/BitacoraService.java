package com.agrihusac.SysInvent.service.bitacora;

import com.agrihusac.SysInvent.model.request.BitacoraRequest;
import com.agrihusac.SysInvent.model.projection.BitacoraProjection;
import com.agrihusac.SysInvent.utils.CustomPage;
import com.agrihusac.SysInvent.utils.MessageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;

public interface BitacoraService {

    ResponseEntity<MessageResponse> registrar(BitacoraRequest request);

    CustomPage<BitacoraProjection> listar(String busqueda, String modulo, String accion,
                                          LocalDate fechaDesde, LocalDate fechaHasta, Pageable pageable);
}
