package com.agrihusac.SysInvent.model.response.almacen;

import com.fasterxml.jackson.databind.JsonNode;

/** Derived movement placeholder; not a persisted Kardex entity or established API contract. */
public record MovimientoAlmacenResponse(JsonNode payload) {
}
