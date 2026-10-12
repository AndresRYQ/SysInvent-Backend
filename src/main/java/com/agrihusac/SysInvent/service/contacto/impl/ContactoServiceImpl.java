package com.agrihusac.SysInvent.service.contacto.impl;

import com.agrihusac.SysInvent.model.entity.ContactoEntity;
import com.agrihusac.SysInvent.model.request.contacto.ContactoRequest;
import com.agrihusac.SysInvent.model.response.contacto.ContactoResponse;
import com.agrihusac.SysInvent.repository.ContactoRepository;
import com.agrihusac.SysInvent.model.entity.ProveedorEntity;
import com.agrihusac.SysInvent.repository.ProveedorRepository;
import com.agrihusac.SysInvent.service.catalogo.AbstractCatalogoService;
import com.agrihusac.SysInvent.service.contacto.ContactoService;
import com.agrihusac.SysInvent.utils.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ContactoServiceImpl extends AbstractCatalogoService<ContactoEntity, ContactoRequest, ContactoResponse> implements ContactoService {
    private final ContactoRepository repository;
    private final ProveedorRepository proveedorRepository;

    public ContactoServiceImpl(ContactoRepository repository, ProveedorRepository proveedorRepository) {
        super(repository);
        this.repository = repository;
        this.proveedorRepository = proveedorRepository;
    }

    @Override
    protected ContactoEntity crearEntidad(ContactoRequest request) {
        ContactoEntity entity = new ContactoEntity();
        entity.setProveedor(proveedorRepository.findById(request.proveedorId()).orElseThrow());
        entity.setNombreCompleto(request.nombreCompleto());
        entity.setCargo(request.cargo());
        entity.setTelefono(request.telefono());
        entity.setCorreo(request.correo());
        entity.setActivo(true);
        return entity;
    }

    @Override
    protected void actualizarEntidad(ContactoEntity entity, ContactoRequest request) {
        entity.setProveedor(proveedorRepository.findById(request.proveedorId()).orElseThrow());
        entity.setNombreCompleto(request.nombreCompleto());
        entity.setCargo(request.cargo());
        entity.setTelefono(request.telefono());
        entity.setCorreo(request.correo());
    }

    @Override
    protected ContactoResponse respuesta(ContactoEntity entity) {
        return new ContactoResponse(entity.getContactoId(), entity.getProveedor().getProveedorId(), entity.getNombreCompleto(), entity.getCargo(), entity.getTelefono(), entity.getCorreo(), entity.getActivo(), entity.getFechaRegistro());
    }

    @Override
    protected boolean duplicado(ContactoRequest request, Integer idExcluido) {
        return false;
    }

    @Override
    protected Page<ContactoEntity> buscarActivos(String filtro, Pageable pageable) {
        return repository.findByActivoTrueAndNombreCompletoContainingIgnoreCase(filtro, pageable);
    }

    @Override
    protected String nombreCatalogo() { return "Contacto"; }

    @Override
    protected void establecerActivo(ContactoEntity entity, boolean activo) { entity.setActivo(activo); }

    @Override
    protected boolean estaActivo(ContactoEntity entity) { return Boolean.TRUE.equals(entity.getActivo()); }

    @Override
    protected ResponseEntity<Object> validar(ContactoRequest request) {
        if (proveedorRepository.findById(request.proveedorId())
                .filter(proveedor -> Boolean.TRUE.equals(proveedor.getActivo())).isEmpty()) {
            return MessageResponse.setResponse(false, HttpStatus.NOT_FOUND, "No se encontro un proveedor activo con ese id");
        }
        return null;
    }
}
