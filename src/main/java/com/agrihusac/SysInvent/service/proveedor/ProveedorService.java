package com.agrihusac.SysInvent.service.proveedor;

import com.agrihusac.SysInvent.model.request.proveedor.ProveedorRequest;
import com.agrihusac.SysInvent.model.response.proveedor.ProveedorResponse;
import com.agrihusac.SysInvent.service.catalogo.CatalogoService;

public interface ProveedorService extends CatalogoService<ProveedorRequest, ProveedorResponse> {}
