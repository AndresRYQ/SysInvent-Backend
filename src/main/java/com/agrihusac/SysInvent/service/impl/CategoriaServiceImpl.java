package com.agrihusac.SysInvent.service.impl;

import com.agrihusac.SysInvent.model.entity.CategoriaEntity;
import com.agrihusac.SysInvent.model.mapper.GlobalMapper;
import com.agrihusac.SysInvent.model.request.CategoriaRequest;
import com.agrihusac.SysInvent.model.response.CategoriaResponse;
import com.agrihusac.SysInvent.repository.CategoriaRepository;
import com.agrihusac.SysInvent.service.CategoriaService;
import com.agrihusac.SysInvent.utils.CustomPage;
import com.agrihusac.SysInvent.utils.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private static final String MSG_REGISTRADA = "Categoría registrada correctamente";
    private static final String MSG_ACTUALIZADA = "Categoría actualizada correctamente";
    private static final String MSG_ELIMINADA = "Categoría eliminada correctamente";
    private static final String MSG_NO_ENCONTRADA = "No se encontró la categoría";

    private final CategoriaRepository categoriaRepository;
    private final GlobalMapper globalMapper;

    @Override
    @Transactional
    public ResponseEntity<Object> registrarActualizarCategoria(CategoriaRequest request) {
        CategoriaEntity categoria;
        HttpStatus status;
        String mensaje;

        if (request.getCategoriaId() == 0) {
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
                return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_NO_ENCONTRADA);
            }

            categoria.setNombre(request.getNombre());
            categoria.setDescripcion(request.getDescripcion());
            status = HttpStatus.OK;
            mensaje = MSG_ACTUALIZADA;
        }

        categoriaRepository.save(categoria);
        return MessageResponse.setResponse(Boolean.TRUE, status, mensaje);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> eliminarCategoria(Integer categoriaId) {
        CategoriaEntity categoria = categoriaRepository.findById(categoriaId).orElse(null);

        if (categoria == null || Boolean.FALSE.equals(categoria.getActivo())) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_NO_ENCONTRADA);
        }

        categoria.setActivo(Boolean.FALSE);
        categoriaRepository.save(categoria);
        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, MSG_ELIMINADA);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomPage<CategoriaResponse> listarCategorias(String nombre, Pageable pageable) {
        Page<CategoriaResponse> categorias = categoriaRepository
                .findByActivoTrueAndNombreContainingIgnoreCase(nombre == null ? "" : nombre, pageable)
                .map(categoria -> globalMapper.map(categoria, CategoriaResponse.class));
        return new CustomPage<>(categorias);
    }
}
