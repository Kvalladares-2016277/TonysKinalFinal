
package org.kennethvalladares.bean;

/**
 *
 * @author luis valladares
 */
public class TipoPlato {
    private int codigoTipoPlato;
    private String descripcionTipo;

    public TipoPlato(int codigoTipoPlato, String descripcionTipo) {
        this.codigoTipoPlato = codigoTipoPlato;
        this.descripcionTipo = descripcionTipo;
    }

    public int getCodigoTipoPlato() {
        return codigoTipoPlato;
    }

    public void setCodigoTipoPlato(int codigoTipoPlato) {
        this.codigoTipoPlato = codigoTipoPlato;
    }

    public String getDescripcionTipo() {
        return descripcionTipo;
    }

    public void setDescripcionTipo(String descripcionTipo) {
        this.descripcionTipo = descripcionTipo;
    }
    
    
 public TipoPlato() {
    }
    public String toString(){
    return getCodigoTipoPlato() + " | " + getDescripcionTipo();
    }
}




