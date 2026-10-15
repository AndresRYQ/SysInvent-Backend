package com.agrihusac.SysInvent.service.proveedor.impl;

import com.agrihusac.SysInvent.model.entity.ProveedorEntity;
import com.agrihusac.SysInvent.model.request.proveedor.ProveedorRequest;
import com.agrihusac.SysInvent.model.response.proveedor.ProveedorResponse;
import com.agrihusac.SysInvent.repository.ProveedorRepository;
import com.agrihusac.SysInvent.service.catalogo.AbstractCatalogoService;
import com.agrihusac.SysInvent.service.proveedor.ProveedorService;
import com.agrihusac.SysInvent.utils.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProveedorServiceImpl extends AbstractCatalogoService<ProveedorEntity, ProveedorRequest, ProveedorResponse> implements ProveedorService {
    private final ProveedorRepository repository;

    public ProveedorServiceImpl(ProveedorRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    protected ProveedorEntity crearEntidad(ProveedorRequest request) {
        ProveedorEntity entity = new ProveedorEntity();
        entity.setRuc(request.ruc());
        entity.setRazonSocial(request.razonSocial());
        entity.setCorreo(request.correo());
        entity.setTelefono(request.telefono());
        entity.setDireccion(request.direccion());
        entity.setActivo(true);
        return entity;
    }

    @Override
    protected void actualizarEntidad(ProveedorEntity entity, ProveedorRequest request) {
        entity.setRuc(request.ruc());
        entity.setRazonSocial(request.razonSocial());
        entity.setCorreo(request.correo());
        entity.setTelefono(request.telefono());
        entity.setDireccion(request.direccion());
    }

    @Override
    protected ProveedorResponse respuesta(ProveedorEntity entity) {
        return new ProveedorResponse(entity.getProveedorId(), entity.getRuc(), entity.getRazonSocial(), entity.getCorreo(), entity.getTelefono(), entity.getDireccion(), entity.getActivo(), entity.getFechaRegistro());
    }

    @Override
    protected boolean duplicado(ProveedorRequest request, Integer idExcluido) {
        return idExcluido == null ? repository.existsByRucIgnoreCase(request.ruc()) : repository.existsByRucIgnoreCaseAndProveedorIdNot(request.ruc(), idExcluido);
    }

    @Override
    protected Page<ProveedorEntity> buscarActivos(String filtro, Pageable pageable) {
        return repository.findByActivoTrueAndRazonSocialContainingIgnoreCase(filtro, pageable);
    }

    @Override
    protected String nombreCatalogo() { return "Proveedor"; }

    @Override
    protected void establecerActivo(ProveedorEntity entity, boolean activo) { entity.setActivo(activo); }

    @Override
    protected boolean estaActivo(ProveedorEntity entity) { return Boolean.TRUE.equals(entity.getActivo()); }

    @Override
    protected ResponseEntity<Object> validar(ProveedorRequest request) {
        return null;
    }
}
