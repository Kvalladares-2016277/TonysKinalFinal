
package org.kennethvalladares.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.kennethvalladares.system.Principal;


public class MenuPrincipalController implements Initializable{
    private Principal escenarioPrincipal;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void mostrarAcercaDe(){
        escenarioPrincipal.mostrarAcercaDe();
    }
    
    public void VentanaEmpresas(){
         escenarioPrincipal.VentanaEmpresas();
    }
    public void VentanaPresupuesto(){
          escenarioPrincipal.VentanaPresupuesto();
    }
    public void VentanaServicio(){
        escenarioPrincipal.VentanaServicio();
    }
    public void VentanaTipoEmpleado(){
        escenarioPrincipal.VentanaTipoEmpleado();
    }
   public void VentanaEmpleado(){
       escenarioPrincipal.VentanaEmpleado();
   }
   public void VentanaTipoPlato(){
       escenarioPrincipal.VentanaTipoPlato();
   }
   public void VentanaPlatos(){
       escenarioPrincipal.VentanaPlatos();
   }
   public void VentanaProductos(){
       escenarioPrincipal.VentanaProductos();
   }
   public void VentanaServicios_has_Empleados(){
       escenarioPrincipal.VentanaServicios_has_Empleados();
   }
   public void VentanaServicios_has_Platos(){
       escenarioPrincipal.VentanaServicios_has_Platos();
   }
   public void VentanaProductos_has_platos(){
       escenarioPrincipal.VentanaProductos_has_platos();
   }
}

