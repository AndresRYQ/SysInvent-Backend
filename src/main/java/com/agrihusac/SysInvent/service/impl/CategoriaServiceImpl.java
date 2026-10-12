package com.agrihusac.SysInvent.service.impl;

import com.agrihusac.SysInvent.model.entity.CategoriaEntity;
import com.agrihusac.SysInvent.model.mapper.GlobalMapper;
import com.agrihusac.SysInvent.model.request.CategoriaRequest;
import com.agrihusac.SysInvent.model.request.catalogo.CategoriaCatalogoRequest;
import com.agrihusac.SysInvent.model.response.CategoriaResponse;
import com.agrihusac.SysInvent.model.response.catalogo.CategoriaCatalogoResponse;
import com.agrihusac.SysInvent.repository.CategoriaRepository;
import com.agrihusac.SysInvent.service.CategoriaService;
import com.agrihusac.SysInvent.service.catalogo.AbstractCatalogoService;
import com.agrihusac.SysInvent.utils.CustomPage;
import com.agrihusac.SysInvent.utils.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaServiceImpl extends
        AbstractCatalogoService<CategoriaEntity, CategoriaCatalogoRequest, CategoriaCatalogoResponse>
        implements CategoriaService {

    private static final String MSG_REGISTRADA = "Categoría registrada correctamente";
    private static final String MSG_ACTUALIZADA = "Categoría actualizada correctamente";
    private static final String MSG_ELIMINADA = "Categoría eliminada correctamente";
    private static final String MSG_NO_ENCONTRADA = "No se encontró la categoría";

    private final CategoriaRepository categoriaRepository;
    private final GlobalMapper globalMapper;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository, GlobalMapper globalMapper) {
        super(categoriaRepository);
        this.categoriaRepository = categoriaRepository;
        this.globalMapper = globalMapper;
    }

    @Override
    protected CategoriaEntity crearEntidad(CategoriaCatalogoRequest request) {
        return CategoriaEntity.builder()
                .nombre(request.nombre())
                .descripcion(request.descripcion())
                .activo(Boolean.TRUE)
                .build();
    }

    @Override
    protected void actualizarEntidad(CategoriaEntity entity, CategoriaCatalogoRequest request) {
        entity.setNombre(request.nombre());
        entity.setDescripcion(request.descripcion());
    }

    @Override
    protected CategoriaCatalogoResponse respuesta(CategoriaEntity entity) {
        return new CategoriaCatalogoResponse(entity.getCategoriaId(), entity.getNombre(),
                entity.getDescripcion(), entity.getActivo(), entity.getFechaRegistro());
    }

    @Override
    protected boolean duplicado(CategoriaCatalogoRequest request, Integer idExcluido) {
        return idExcluido == null
                ? categoriaRepository.existsByNombreIgnoreCase(request.nombre())
                : categoriaRepository.existsByNombreIgnoreCaseAndCategoriaIdNot(request.nombre(), idExcluido);
    }

    @Override
    protected Page<CategoriaEntity> buscarActivos(String filtro, Pageable pageable) {
        return categoriaRepository.findByActivoTrueAndNombreContainingIgnoreCase(filtro, pageable);
    }

    @Override
    protected String nombreCatalogo() {
        return "Categoría";
    }

    @Override
    protected void establecerActivo(CategoriaEntity entity, boolean activo) {
        entity.setActivo(activo);
    }

    @Override
    protected boolean estaActivo(CategoriaEntity entity) {
        return Boolean.TRUE.equals(entity.getActivo());
    }

    @Override
    @Transactional
    public ResponseEntity<Object> registrarActualizarCategoria(CategoriaRequest request) {
        CategoriaEntity categoria;
        HttpStatus status;
        String mensaje;

        if (request.getCategoriaId() == 0) {
            if (categoriaRepository.existsByNombreIgnoreCase(request.getNombre())) {
                return MessageResponse.setResponse(false, HttpStatus.CONFLICT,
                        "Ya existe una categoría con ese nombre");
            }
            categoria = CategoriaEntity.builder()
                    .nombre(request.getNombre())
                    .descripcion(request.getDescripcion())
                    .activo(Boolean.TRUE)
                    .build();
            status = HttpStatus.CREATED;
            mensaje = MSG_REGISTRADA;
        } else {
            categoria = categoriaRepository.findById(request.getCategoriaId()).orElse(null);
            if (categoria == null || Boolean.FALSE.equals(categoria.getActivo())) {
                return MessageResponse.setResponse(false, HttpStatus.NOT_FOUND, MSG_NO_ENCONTRADA);
            }
            if (categoriaRepository.existsByNombreIgnoreCaseAndCategoriaIdNot(
                    request.getNombre(), request.getCategoriaId())) {
                return MessageResponse.setResponse(false, HttpStatus.CONFLICT,
                        "Ya existe una categoría con ese nombre");
            }
            categoria.setNombre(request.getNombre());
            categoria.setDescripcion(request.getDescripcion());
            status = HttpStatus.OK;
            mensaje = MSG_ACTUALIZADA;
        }

        categoria = categoriaRepository.saveAndFlush(categoria);
        return MessageResponse.setResponse(true, status, mensaje,
                globalMapper.map(categoria, CategoriaResponse.class));
    }

    @Override
    @Transactional
    public ResponseEntity<Object> eliminarCategoria(Integer categoriaId) {
        CategoriaEntity categoria = categoriaRepository.findById(categoriaId).orElse(null);
        if (categoria == null || Boolean.FALSE.equals(categoria.getActivo())) {
            return MessageResponse.setResponse(false, HttpStatus.NOT_FOUND, MSG_NO_ENCONTRADA);
        }
        categoria.setActivo(Boolean.FALSE);
        categoria = categoriaRepository.saveAndFlush(categoria);
        return MessageResponse.setResponse(true, HttpStatus.OK, MSG_ELIMINADA,
                globalMapper.map(categoria, CategoriaResponse.class));
    }

    @Override
    @Transactional(readOnly = true)
    public CustomPage<CategoriaResponse> listarCategorias(String nombre, Pageable pageable) {
        Page<CategoriaResponse> categorias = categoriaRepository
                .findByActivoTrueAndNombreContainingIgnoreCase(nombre == null ? "" : nombre.trim(), pageable)
                .map(categoria -> globalMapper.map(categoria, CategoriaResponse.class));
        return new CustomPage<>(categorias);
    }
}
