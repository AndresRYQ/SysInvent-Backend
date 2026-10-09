package com.agrihusac.SysInvent.service.contacto;

import com.agrihusac.SysInvent.model.request.contacto.ContactoRequest;
import com.agrihusac.SysInvent.model.response.contacto.ContactoResponse;
import com.agrihusac.SysInvent.service.catalogo.CatalogoService;

public interface ContactoService extends CatalogoService<ContactoRequest, ContactoResponse> {}
