package org.kennethvalladares.bean;

import java.util.Date;


public class Presupuesto {
    private Integer codigoPresupuesto;
    private Date fechaSolicitud;
    private float cantidadPresupuesto;
    private Integer codigoEmpresa;

    public Presupuesto() {
    }

    public Presupuesto(Integer codigoPresupuesto, Date fechaSolicitud, Float cantidadPresupuesto, Integer codigoEmpresa) {
        this.codigoPresupuesto = codigoPresupuesto;
        this.fechaSolicitud = fechaSolicitud;
        this.cantidadPresupuesto = cantidadPresupuesto;
        this.codigoEmpresa = codigoEmpresa;
    }

    public Integer getCodigoPresupuesto() {
        return codigoPresupuesto;
    }

    public void setCodigoPresupuesto(Integer codigoPresupuesto) {
        this.codigoPresupuesto = codigoPresupuesto;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public Float getCantidadPresupuesto() {
        return cantidadPresupuesto;
    }

    public void setCantidadPresupuesto(Float cantidadPresupuesto) {
        this.cantidadPresupuesto = cantidadPresupuesto;
    }

    public Integer getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(Integer codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }
}
