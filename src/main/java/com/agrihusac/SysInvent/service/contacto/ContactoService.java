package com.agrihusac.SysInvent.service.contacto;

import com.agrihusac.SysInvent.model.request.contacto.ContactoRequest;
import com.agrihusac.SysInvent.model.response.contacto.ContactoResponse;
import com.agrihusac.SysInvent.service.catalogo.CatalogoService;
import com.agrihusac.SysInvent.utils.CustomPage;
import org.springframework.data.domain.Pageable;

public interface ContactoService extends CatalogoService<ContactoRequest, ContactoResponse> {
    CustomPage<ContactoResponse> listarPorProveedor(String filtro, Integer proveedorId, Pageable pageable);
}
