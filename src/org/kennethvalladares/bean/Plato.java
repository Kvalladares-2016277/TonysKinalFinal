package org.kennethvalladares.bean;


public class Plato {
     private Integer codigoPlato;
    private Integer cantidad;
    private String nombrePlato;
    private String descripcionPlato;
    private Float precioPlato;
    private Integer codigoTipoPlato;

    public Plato() {
    }

    public Plato(Integer codigoPlato, Integer cantidad, String nombrePlato, String descripcionPlato, Float precioPlato, Integer codigoTipoPlato) {
        this.codigoPlato = codigoPlato;
        this.cantidad = cantidad;
        this.nombrePlato = nombrePlato;
        this.descripcionPlato = descripcionPlato;
        this.precioPlato = precioPlato;
        this.codigoTipoPlato = codigoTipoPlato;
    }

    public Integer getCodigoPlato() {
        return codigoPlato;
    }

    public void setCodigoPlato(Integer codigoPlato) {
        this.codigoPlato = codigoPlato;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombrePlato() {
        return nombrePlato;
    }

    public void setNombrePlato(String nombrePlato) {
        this.nombrePlato = nombrePlato;
    }

    public String getDescripcionPlato() {
        return descripcionPlato;
    }

    public void setDescripcionPlato(String descripcionPlato) {
        this.descripcionPlato = descripcionPlato;
    }

    public Float getPrecioPlato() {
        return precioPlato;
    }

    public void setPrecioPlato(Float precioPlato) {
        this.precioPlato = precioPlato;
    }

    public Integer getCodigoTipoPlato() {
        return codigoTipoPlato;
    }

    public void setCodigoTipoPlato(Integer codigoTipoPlato) {
        this.codigoTipoPlato = codigoTipoPlato;
    }
    public String toString(){
        return getCodigoPlato() + " | " +  getNombrePlato();
    }
}
