package com.agrihusac.SysInvent.service.unidadmedida.impl;

import com.agrihusac.SysInvent.model.entity.UnidadMedidaEntity;
import com.agrihusac.SysInvent.model.request.unidadmedida.UnidadMedidaRequest;
import com.agrihusac.SysInvent.model.response.unidadmedida.UnidadMedidaResponse;
import com.agrihusac.SysInvent.repository.UnidadMedidaRepository;
import com.agrihusac.SysInvent.service.catalogo.AbstractCatalogoService;
import com.agrihusac.SysInvent.service.unidadmedida.UnidadMedidaService;
import com.agrihusac.SysInvent.utils.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UnidadMedidaServiceImpl extends AbstractCatalogoService<UnidadMedidaEntity, UnidadMedidaRequest, UnidadMedidaResponse> implements UnidadMedidaService {
    private final UnidadMedidaRepository repository;

    public UnidadMedidaServiceImpl(UnidadMedidaRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    protected UnidadMedidaEntity crearEntidad(UnidadMedidaRequest request) {
        UnidadMedidaEntity entity = new UnidadMedidaEntity();
        entity.setNombre(request.nombre());
        entity.setDescripcion(request.descripcion());
        entity.setActivo(true);
        return entity;
    }

    @Override
    protected void actualizarEntidad(UnidadMedidaEntity entity, UnidadMedidaRequest request) {
        entity.setNombre(request.nombre());
        entity.setDescripcion(request.descripcion());
    }

    @Override
    protected UnidadMedidaResponse respuesta(UnidadMedidaEntity entity) {
        return new UnidadMedidaResponse(entity.getUnidadMedidaId(), entity.getNombre(), entity.getDescripcion(), entity.getActivo(), entity.getFechaRegistro());
    }

    @Override
    protected boolean duplicado(UnidadMedidaRequest request, Integer idExcluido) {
        return idExcluido == null ? repository.existsByNombreIgnoreCase(request.nombre()) : repository.existsByNombreIgnoreCaseAndUnidadMedidaIdNot(request.nombre(), idExcluido);
    }

    @Override
    protected Page<UnidadMedidaEntity> buscarActivos(String filtro, Pageable pageable) {
        return repository.findByActivoTrueAndNombreContainingIgnoreCase(filtro, pageable);
    }

    @Override
    protected String nombreCatalogo() { return "Unidad de medida"; }

    @Override
    protected void establecerActivo(UnidadMedidaEntity entity, boolean activo) { entity.setActivo(activo); }

    @Override
    protected boolean estaActivo(UnidadMedidaEntity entity) { return Boolean.TRUE.equals(entity.getActivo()); }

    @Override
    protected ResponseEntity<Object> validar(UnidadMedidaRequest request) {
        return null;
    }
}
