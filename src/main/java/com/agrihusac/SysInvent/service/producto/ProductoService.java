package com.agrihusac.SysInvent.service.producto;

import com.agrihusac.SysInvent.model.request.producto.ProductoRequest;
import com.agrihusac.SysInvent.model.response.producto.ProductoResponse;
import com.agrihusac.SysInvent.service.catalogo.CatalogoService;
import com.agrihusac.SysInvent.utils.CustomPage;
import org.springframework.data.domain.Pageable;

public interface ProductoService
        extends CatalogoService<ProductoRequest, ProductoResponse> {

    CustomPage<ProductoResponse> listar(
            String filtro,
            Integer tipoProductoId,
            Integer categoriaId,
            Boolean estado,
            Pageable pageable
    );
}