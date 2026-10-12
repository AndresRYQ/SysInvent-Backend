package com.agrihusac.SysInvent.service;

import com.agrihusac.SysInvent.model.request.CategoriaRequest;
import com.agrihusac.SysInvent.model.response.CategoriaResponse;
import com.agrihusac.SysInvent.model.request.catalogo.CategoriaCatalogoRequest;
import com.agrihusac.SysInvent.model.response.catalogo.CategoriaCatalogoResponse;
import com.agrihusac.SysInvent.service.catalogo.CatalogoService;
import com.agrihusac.SysInvent.utils.CustomPage;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface CategoriaService extends CatalogoService<CategoriaCatalogoRequest, CategoriaCatalogoResponse> {

    ResponseEntity<Object> registrarActualizarCategoria(CategoriaRequest request);

    ResponseEntity<Object> eliminarCategoria(Integer categoriaId);

    CustomPage<CategoriaResponse> listarCategorias(String nombre, Pageable pageable);
}
