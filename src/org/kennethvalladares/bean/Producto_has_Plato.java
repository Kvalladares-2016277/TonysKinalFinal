package org.kennethvalladares.bean;


public class Producto_has_Plato {
    private Integer codigoProducto;
    private Integer codigoPlato;

    public Producto_has_Plato() {
    }

    public Producto_has_Plato(Integer codigoProducto, Integer codigoPlato) {
        this.codigoProducto = codigoProducto;
        this.codigoPlato = codigoPlato;
    }

    public Integer getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(Integer codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public Integer getCodigoPlato() {
        return codigoPlato;
    }

    public void setCodigoPlato(Integer codigoPlato) {
        this.codigoPlato = codigoPlato;
    }
}
