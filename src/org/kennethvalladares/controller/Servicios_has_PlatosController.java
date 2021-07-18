
package org.kennethvalladares.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.kennethvalladares.bean.Plato;
import org.kennethvalladares.bean.Servicio;
import org.kennethvalladares.bean.Servicio_has_Plato;
import org.kennethvalladares.db.Conexion;
import org.kennethvalladares.system.Principal;

/**
 *
 * @author luis valladares
 */
public class Servicios_has_PlatosController implements Initializable {
    private Principal escenarioPrincipal;
    private ObservableList<Servicio_has_Plato> listaServicio_has_Plato;
    private ObservableList<Servicio> listaServicio;
    private ObservableList<Plato> listaPlato;
    
    @FXML private TableView tblServiciosPlatos;
    @FXML private TableColumn colServiciosCodigoServicio;
    @FXML private TableColumn colPlatosCodigoPlato;
    @FXML private ComboBox cmbServicioCodigoServicio;
    @FXML private ComboBox cmbPlatosCodigoPlato;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        this.cmbPlatosCodigoPlato.setItems(getPlato());
        this.cmbServicioCodigoServicio.setItems(getServicio());
    }
    
    public void cargarDatos(){
        tblServiciosPlatos.setItems(getServicioPlato());
        colServiciosCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicio_has_Plato ,Integer>("codigoServicio"));
        colPlatosCodigoPlato.setCellValueFactory(new PropertyValueFactory<Servicio_has_Plato, Integer>("codigoPlato"));
    }
    
    public ObservableList<Servicio_has_Plato> getServicioPlato(){
        ArrayList<Servicio_has_Plato> lista = new ArrayList<Servicio_has_Plato>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServiciosPlatos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicio_has_Plato(resultado.getInt("Servicios_codigoServicio"),resultado.getInt("Platos_codigoPlato")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaServicio_has_Plato = FXCollections.observableArrayList(lista);
    }
  public ObservableList<Servicio> getServicio(){
        ArrayList<Servicio> lista = new ArrayList<Servicio>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicio}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicio(resultado.getInt("codigoServicio"), 
                                       resultado.getDate("fechaServicio"), 
                                       resultado.getString("tipoServicio"), 
                                       resultado.getString("horaServicio"), 
                                       resultado.getString("lugarServicio"), 
                                       resultado.getString("telefonoContacto"), 
                                       resultado.getInt("codigoEmpresa")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaServicio = FXCollections.observableArrayList(lista);
    }
      public ObservableList<Plato> getPlato(){
        ArrayList<Plato> lista = new ArrayList<Plato>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPlato}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Plato(resultado.getInt("codigoPlato"), 
                                    resultado.getInt("cantidad"), 
                                    resultado.getString("nombrePlato"), 
                                    resultado.getString("descripcionPlato"), 
                                    resultado.getFloat("precioPlato"), 
                                    resultado.getInt("codigoTipoPlato")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaPlato = FXCollections.observableArrayList(lista);
    }
      public Servicio buscarServicio(int codigoServicio){
          Servicio resultado = null;
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarServicio(?)}");
                procedimiento.setInt(1, codigoServicio);
                ResultSet registro = procedimiento.executeQuery();
                while(registro.next()){
                    resultado = new Servicio(
                    registro.getInt("codigoServicio"),
                    registro.getDate("fechaServicio"),
                    registro.getString("tipoServicio"),
                    registro.getString("horaServicio"),
                    registro.getString("lugarServicio"),
                    registro.getString("telefonoContacto"),
                    registro.getInt("codigoEmpresa"));
                    
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return resultado;
      }
      public Plato buscarPlato(int codigoPlato){
          Plato resultado = null;
          try{
              PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarPlato(?)}");
              procedimiento.setInt(1, codigoPlato);
              ResultSet registro = procedimiento.executeQuery();
              while(registro.next()){
                  resultado = new Plato (registro.getInt("codigoPlato"),
                    registro.getInt("cantidad"),
                    registro.getString("nombrePlato"),
                    registro.getString("descripcionPlato"),
                    registro.getFloat("precioPlato"),
                    registro.getInt("codigoTipoPlato"));
                  
              }
          }catch(Exception e){
              e.printStackTrace();
          }
          return resultado;
      }
      public void seleccionarElemento(){
          cmbServicioCodigoServicio.getSelectionModel().select(buscarServicio(((Servicio_has_Plato)tblServiciosPlatos.getSelectionModel().getSelectedItem()).getCodigoServicio()));
          cmbPlatosCodigoPlato.getSelectionModel().select(buscarPlato(((Servicio_has_Plato)tblServiciosPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
      }
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void salir(){
        this.escenarioPrincipal.menuPrincipal();
    }
}
