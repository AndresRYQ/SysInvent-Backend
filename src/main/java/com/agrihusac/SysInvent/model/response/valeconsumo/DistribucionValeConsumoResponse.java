package com.agrihusac.SysInvent.model.response.valeconsumo;

import java.math.BigDecimal;

public record DistribucionValeConsumoResponse(
        Integer id,
        Integer destinoId,
        String destino,
        Integer parteEquipoId,
        String parteEquipo,
        BigDecimal cantidad
) {
}
