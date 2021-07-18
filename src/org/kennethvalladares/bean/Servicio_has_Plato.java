package org.kennethvalladares.bean;


public class Servicio_has_Plato {
     private Integer codigoServicio;
    private Integer codigoPlato;

    public Servicio_has_Plato() {
    }

    public Servicio_has_Plato(Integer codigoServicio, Integer codigoPlato) {
        this.codigoServicio = codigoServicio;
        this.codigoPlato = codigoPlato;
    }

    public Integer getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(Integer codigoServicio) {
        this.codigoServicio = codigoServicio;
    }

    public Integer getCodigoPlato() {
        return codigoPlato;
    }

    public void setCodigoPlato(Integer codigoPlato) {
        this.codigoPlato = codigoPlato;
    }
}
