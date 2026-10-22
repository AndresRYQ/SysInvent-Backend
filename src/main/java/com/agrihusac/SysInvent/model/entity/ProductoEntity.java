package com.agrihusac.SysInvent.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "producto")
@Getter
@Setter
@NoArgsConstructor
public class ProductoEntity extends AuditoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "producto_id")
    private Integer productoId;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Column(name = "codigo", nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "tipo_producto_id", nullable = false)
    private Integer tipoProductoId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_producto_id", insertable = false, updatable = false)
    private TipoProductoEntity tipoProducto;

    public void setTipoProducto(TipoProductoEntity tipoProducto) {
        this.tipoProducto = tipoProducto;
        this.tipoProductoId = tipoProducto == null ? null : tipoProducto.getTipoProductoId();
    }

    @Column(name = "categoria_id", nullable = false)
    private Integer categoriaId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_id", insertable = false, updatable = false)
    private CategoriaEntity categoria;

    public void setCategoria(CategoriaEntity categoria) {
        this.categoria = categoria;
        this.categoriaId = categoria == null ? null : categoria.getCategoriaId();
    }

    @Column(name = "unidad_medida_id", nullable = false)
    private Integer unidadMedidaId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "unidad_medida_id", insertable = false, updatable = false)
    private UnidadMedidaEntity unidadMedida;

    public void setUnidadMedida(UnidadMedidaEntity unidadMedida) {
        this.unidadMedida = unidadMedida;
        this.unidadMedidaId = unidadMedida == null ? null : unidadMedida.getUnidadMedidaId();
    }

    @Column(name = "proveedor_id", nullable = false)
    private Integer proveedorId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "proveedor_id", insertable = false, updatable = false)
    private ProveedorEntity proveedor;

    public void setProveedor(ProveedorEntity proveedor) {
        this.proveedor = proveedor;
        this.proveedorId = proveedor == null ? null : proveedor.getProveedorId();
    }

    @Column(name = "stock_actual", nullable = false, precision = 18, scale = 4)
    private BigDecimal stockActual;

    @Column(name = "stock_minimo", nullable = false, precision = 18, scale = 4)
    private BigDecimal stockMinimo;

    @Column(name = "precio_unitario", nullable = false, precision = 18, scale = 4)
    private BigDecimal precioUnitario;
}
