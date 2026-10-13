package com.agrihusac.SysInvent.service.producto;

import com.agrihusac.SysInvent.model.request.producto.ProductoRequest;
import com.agrihusac.SysInvent.model.response.producto.ProductoResponse;
import com.agrihusac.SysInvent.service.catalogo.CatalogoService;

public interface ProductoService extends CatalogoService<ProductoRequest, ProductoResponse> {}
