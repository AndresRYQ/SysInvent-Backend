package com.agrihusac.SysInvent.service.destino.impl;

import com.agrihusac.SysInvent.model.entity.DestinoEntity;
import com.agrihusac.SysInvent.model.request.destino.DestinoRequest;
import com.agrihusac.SysInvent.model.response.destino.DestinoResponse;
import com.agrihusac.SysInvent.repository.DestinoRepository;
import com.agrihusac.SysInvent.service.catalogo.AbstractCatalogoService;
import com.agrihusac.SysInvent.service.destino.DestinoService;
import com.agrihusac.SysInvent.utils.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DestinoServiceImpl extends AbstractCatalogoService<DestinoEntity, DestinoRequest, DestinoResponse> implements DestinoService {
    private final DestinoRepository repository;

    public DestinoServiceImpl(DestinoRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    protected DestinoEntity crearEntidad(DestinoRequest request) {
        DestinoEntity entity = new DestinoEntity();
        entity.setNombre(request.nombre());
        entity.setDescripcion(request.descripcion());
        entity.setActivo(true);
        return entity;
    }

    @Override
    protected void actualizarEntidad(DestinoEntity entity, DestinoRequest request) {
        entity.setNombre(request.nombre());
        entity.setDescripcion(request.descripcion());
    }

    @Override
    protected DestinoResponse respuesta(DestinoEntity entity) {
        return new DestinoResponse(entity.getDestinoId(), entity.getNombre(), entity.getDescripcion(), entity.getActivo(), entity.getFechaRegistro());
    }

    @Override
    protected boolean duplicado(DestinoRequest request, Integer idExcluido) {
        return idExcluido == null ? repository.existsByNombreIgnoreCase(request.nombre()) : repository.existsByNombreIgnoreCaseAndDestinoIdNot(request.nombre(), idExcluido);
    }

    @Override
    protected Page<DestinoEntity> buscarActivos(String filtro, Pageable pageable) {
        return repository.findByActivoTrueAndNombreContainingIgnoreCase(filtro, pageable);
    }

    @Override
    protected String nombreCatalogo() { return "Destino"; }

    @Override
    protected void establecerActivo(DestinoEntity entity, boolean activo) { entity.setActivo(activo); }

    @Override
    protected boolean estaActivo(DestinoEntity entity) { return Boolean.TRUE.equals(entity.getActivo()); }

    @Override
    protected ResponseEntity<Object> validar(DestinoRequest request) {
        return null;
    }
}
