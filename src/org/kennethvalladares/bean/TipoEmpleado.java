
package org.kennethvalladares.bean;

/**
 *
 * @author luis valladares
 */
public class TipoEmpleado {
        private int codigoTipoEmpleado;
        private String descripcion;

    public TipoEmpleado(int codigoTipoEmpleado, String descripcion) {
        this.codigoTipoEmpleado = codigoTipoEmpleado;
        this.descripcion = descripcion;
    }

    public int getCodigoTipoEmpleado() {
        return codigoTipoEmpleado;
    }

    public void setCodigoTipoEmpleado(int codigoTipoEmpleado) {
        this.codigoTipoEmpleado = codigoTipoEmpleado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    
    public TipoEmpleado() {
    }
    public String toString(){
        return getCodigoTipoEmpleado()+ " | " + getDescripcion();
    }
    
}
