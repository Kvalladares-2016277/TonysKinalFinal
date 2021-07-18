/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import org.kennethvalladares.bean.Producto_has_Plato;
import org.kennethvalladares.bean.Productos;
import org.kennethvalladares.db.Conexion;
import org.kennethvalladares.system.Principal;

/**
 *
 * @author luis valladares
 */
public class productos_has_platosController implements Initializable {
     private Principal escenarioPrincipal; 
     private ObservableList<Producto_has_Plato> listaProducto_has_Plato;
     private ObservableList<Productos>ListaProductos;
     private ObservableList<Plato> listaPlato;
     @FXML private TableView tblProductos_has_platos;
     @FXML private TableColumn colProductosCodigoProducto;
     @FXML private TableColumn colPlatosCodigoPlato;
     @FXML private ComboBox cmbProductoCodigoProducto;
     @FXML private ComboBox cmbPlatoCodigoPlato;
     
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        this.cmbPlatoCodigoPlato.setItems(getPlato());
        this.cmbProductoCodigoProducto.setItems(getProductos());
     
    }
    
     public void cargarDatos(){
        tblProductos_has_platos.setItems(getProductoPlato());
        colProductosCodigoProducto.setCellValueFactory(new PropertyValueFactory<Producto_has_Plato, Integer>("codigoProducto"));
        colPlatosCodigoPlato.setCellValueFactory(new PropertyValueFactory<Producto_has_Plato, Integer>("codigoPlato"));
    }
    
    public ObservableList<Producto_has_Plato> getProductoPlato(){
        ArrayList<Producto_has_Plato> lista = new ArrayList<Producto_has_Plato>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductosPlatos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Producto_has_Plato(resultado.getInt("Productos_codigoProducto"), 
                                                 resultado.getInt("Platos_codigoPlato")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaProducto_has_Plato = FXCollections.observableArrayList(lista);
    }
    public ObservableList<Productos> getProductos(){
           ArrayList<Productos> lista = new ArrayList<Productos>();
                try{
                    PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos}");
                    ResultSet resultado = procedimiento.executeQuery();
              while(resultado.next()){
                      lista.add(new Productos (resultado.getInt("codigoProducto"),
                                        resultado.getString("nombreProducto"),
                                        resultado.getInt("cantidad")));
              }
                }catch(Exception e){
                    e.printStackTrace();
                }
                return ListaProductos = FXCollections.observableArrayList(lista);
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
    //buscar de las entidades 
    public Productos buscarProductos(int codigoProducto){
        Productos resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarProducto(?)}");
            procedimiento.setInt(1, codigoProducto);
            ResultSet registro = procedimiento.executeQuery();
                while(registro.next()){
                    resultado = new Productos(
                            registro.getInt("codigoProducto"),
                            registro.getString("nombreProducto"),
                            registro.getInt("cantidad"));
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
                    resultado = new Plato(
                    registro.getInt("codigoPlato"),
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
        cmbProductoCodigoProducto.getSelectionModel().select(buscarProductos(((Producto_has_Plato)tblProductos_has_platos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
        cmbPlatoCodigoPlato.getSelectionModel().select(buscarPlato(((Producto_has_Plato)tblProductos_has_platos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
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
