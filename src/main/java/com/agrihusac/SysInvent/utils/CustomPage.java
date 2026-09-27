package com.agrihusac.SysInvent.utils;

import lombok.Generated;
import org.springframework.data.domain.Page;

import java.util.List;

public class CustomPage<T> {
    private List<T> datos;
    private CustomPageable paginacion;

    public CustomPage() {
    }

    public CustomPage(Page<T> page) {
        this.datos = page.getContent();
        this.paginacion = new CustomPageable(
                page.getPageable().getPageNumber() + 1,
                page.getPageable().getPageSize(),
                page.getTotalElements()
        );
    }

    @Generated
    public List<T> getDatos() {
        return datos;
    }

    @Generated
    public CustomPageable getPaginacion() {
        return paginacion;
    }

    @Generated
    public void setDatos(List<T> datos) {
        this.datos = datos;
    }

    @Generated
    public void setPaginacion(CustomPageable paginacion) {
        this.paginacion = paginacion;
    }

    @Generated
    public static class CustomPageable {

        private int numeroPagina;
        private int tamanioPagina;
        private long totalElementos;

        public CustomPageable() {
        }

        public CustomPageable(int numeroPagina, int tamanioPagina, long totalElementos) {
            this.numeroPagina = numeroPagina;
            this.tamanioPagina = tamanioPagina;
            this.totalElementos = totalElementos;
        }

        public int getNumeroPagina() {
            return numeroPagina;
        }

        public int getTamanioPagina() {
            return tamanioPagina;
        }

        public long getTotalElementos() {
            return totalElementos;
        }

        public void setNumeroPagina(int numeroPagina) {
            this.numeroPagina = numeroPagina;
        }

        public void setTamanioPagina(int tamanioPagina) {
            this.tamanioPagina = tamanioPagina;
        }

        public void setTotalElementos(long totalElementos) {
            this.totalElementos = totalElementos;
        }
    }
}
