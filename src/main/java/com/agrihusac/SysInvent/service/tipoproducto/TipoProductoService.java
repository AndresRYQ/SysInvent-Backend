package com.agrihusac.SysInvent.service.tipoproducto;

import com.agrihusac.SysInvent.model.request.tipoproducto.TipoProductoRequest;
import com.agrihusac.SysInvent.model.response.tipoproducto.TipoProductoResponse;
import com.agrihusac.SysInvent.service.catalogo.CatalogoService;

public interface TipoProductoService extends CatalogoService<TipoProductoRequest, TipoProductoResponse> {}
