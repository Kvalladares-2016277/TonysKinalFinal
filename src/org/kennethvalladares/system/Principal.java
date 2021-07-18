/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kennethvalladares.system;

import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.kennethvalladares.controller.AcercaDeController;
import org.kennethvalladares.controller.EmpleadoController;
import org.kennethvalladares.controller.EmpresaController;
import org.kennethvalladares.controller.MenuPrincipalController;
import org.kennethvalladares.controller.PlatosController;
import org.kennethvalladares.controller.PresupuestoController;
import org.kennethvalladares.controller.ProductosController;
import org.kennethvalladares.controller.ServicioController;
import org.kennethvalladares.controller.Servicios_has_EmpleadosController;
import org.kennethvalladares.controller.Servicios_has_PlatosController;
import org.kennethvalladares.controller.TipoEmpleadoController;
import org.kennethvalladares.controller.TipoPlatoController;
import org.kennethvalladares.controller.productos_has_platosController;

/**
 *
 * @author luis valladares
 */
public class Principal extends Application {
    private Stage escenarioPrincipal;
    private Scene escena;
    private final String PAQUETE_VISTA = "/org/kennethvalladares/view/";
    
    
    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Tony´s Kinal App");
        escenarioPrincipal.getIcons().add(new Image("/org/kennethvalladares/image/icono.png"));
        //Parent root = FXMLLoader.load(getClass().getResource("/org/kennethvalladares/view/MenuPrincipalView.fxml"));
        //Scene escena = new Scene(root);
        //escenarioPrincipal.setScene(escena);
        menuPrincipal();
        escenarioPrincipal.show();
        
        
    }
    public void menuPrincipal(){
        //metodo para levantar su ventana  //agregar APIS
        try{
            MenuPrincipalController menuPrincipal =(MenuPrincipalController) cambiarEscena("MenuPrincipalView.fxml",284,400);
            menuPrincipal.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void VentanaEmpresas(){
        try{
            EmpresaController empresaController = (EmpresaController)cambiarEscena("EmpresaView.fxml",791,537);
            empresaController.setEscenarioPrincipal(this);
        }catch(Exception e) {
               e.printStackTrace();
    }
        }
    public void mostrarAcercaDe(){
        try{
            AcercaDeController acercaDeView = (AcercaDeController)cambiarEscena("AcercaDeView.fxml",541,338);
            acercaDeView.setPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
     
    }
    public void VentanaPresupuesto(){
        try{
            PresupuestoController Presupuesto = (PresupuestoController)cambiarEscena("PresupuestoView.fxml",762,565);
            Presupuesto.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
  public void VentanaServicio(){
        try{
            ServicioController Servicio = (ServicioController)cambiarEscena("ServiciosView.fxml",866,534);
            Servicio.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
  }
  public void VentanaTipoEmpleado(){
      try{
          TipoEmpleadoController TipoEmpleado = (TipoEmpleadoController)cambiarEscena("TipoEmpleadoView.fxml",741,507);
          TipoEmpleado.setEscenarioPrincipal(this);
      }catch(Exception e){
            e.printStackTrace();
      }
  }
  public void VentanaEmpleado(){
      try{
          EmpleadoController Empleado = (EmpleadoController)cambiarEscena("EmpleadoView.fxml",1017,523);
          Empleado.setEscenarioPrincipal(this);
      }catch(Exception e){
          e.printStackTrace();
      }
  }
   public void VentanaTipoPlato(){
       try{
           TipoPlatoController TipoPlato = (TipoPlatoController)cambiarEscena("TipoPlatoView.fxml",747,544);
           TipoPlato.setEscenarioPrincipal(this);
       }catch(Exception e){
           e.printStackTrace();
       }
   }
   public void VentanaPlatos(){
       try{
           PlatosController Platos = (PlatosController)cambiarEscena("PlatosView.fxml",823,534);
           Platos.setEscenarioPrincipal(this);
       }catch(Exception e){
           e.printStackTrace();
       } 
   }
   public void VentanaProductos(){
       try{
           ProductosController Productos = (ProductosController)cambiarEscena("ProductosView.fxml",752,582);
           Productos.setEscenarioPrincipal(this);
       }catch(Exception e){
           e.printStackTrace();
       }
   }
   public void VentanaServicios_has_Empleados(){
       try{
           Servicios_has_EmpleadosController ServiciosEmpleados = (Servicios_has_EmpleadosController)cambiarEscena("Servicios_EmpleadosView.fxml",839,548);
           ServiciosEmpleados.setEscenarioPrincipal(this);
       }catch(Exception e){
           e.printStackTrace();
       }  
   }
    public void VentanaServicios_has_Platos(){
        try{
            Servicios_has_PlatosController Servicios_has_Platos = (Servicios_has_PlatosController)cambiarEscena("Servicios_PlatosView.fxml",754,457);
            Servicios_has_Platos.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void VentanaProductos_has_platos(){
        try{
            productos_has_platosController productos_has_platos = (productos_has_platosController)cambiarEscena("productos_has_platosView.fxml",759,462);
            productos_has_platos.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
    
  
 
    public Initializable cambiarEscena (String fxml, int ancho,int alto)   throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA + fxml); //quien tiene que guardar o levantar el menu
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA + fxml));
        Scene escena = new Scene((AnchorPane)cargadorFXML.load(archivo) ,ancho,alto);
        escena.getStylesheets().add("org/kennethvalladares/resource/FondoEmpresa.css");
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
         return resultado;
     }

    

   
        
    }
    

