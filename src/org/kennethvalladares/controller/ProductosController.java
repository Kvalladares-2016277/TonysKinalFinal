
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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.kennethvalladares.bean.Productos;
import org.kennethvalladares.db.Conexion;
import org.kennethvalladares.system.Principal;

/**
 *
 * @author luis valladares
 */
public class ProductosController implements Initializable {

   
    private enum operaciones {NUEVO,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Productos>ListaProductos;
    
    @FXML private TextField txtCodigoProducto;
    @FXML private TextField txtNombreProducto;
    @FXML private TextField txtCantidad;
    @FXML private TableView tblProductos;
    @FXML private TableColumn colCodigoProducto;
    @FXML private TableColumn colNombreProducto;
    @FXML private TableColumn colCantidad;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
        public void cargarDatos(){
            tblProductos.setItems(getProductos());
            colCodigoProducto.setCellValueFactory(new PropertyValueFactory<Productos,Integer>("codigoProducto"));
            colNombreProducto.setCellValueFactory(new PropertyValueFactory<Productos,String>("nombreProducto"));
            colCantidad.setCellValueFactory(new PropertyValueFactory<Productos,Integer>("cantidad"));
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
        //metodo para los botones
        public void nuevo(){
            switch(tipoDeOperacion){
                case NINGUNO:
                        activarControles();
                           btnNuevo.setText("Guardar");
                           btnEliminar.setDisable(false);
                           btnEliminar.setText("Cancelar");
                           btnEditar.setDisable(true);
                           btnReporte.setDisable(true);
                           tipoDeOperacion = operaciones.GUARDAR;
                    break;
                case GUARDAR:
                    guardar();
                        desactivarControles();
                        limpiarControles();
                        btnNuevo.setText("Nuevo");
                        btnEliminar.setText("Elimnar");
                        btnEliminar.setDisable(true);
                        btnEditar.setDisable(true);
                        btnReporte.setDisable(true);
                        tipoDeOperacion = operaciones.NINGUNO;
                    break;
            }
        }
            //metodo para agregar datos 
    public void guardar(){
        if(txtNombreProducto.getText().isEmpty() == true){
            JOptionPane.showMessageDialog(null, "Por favor ingrese un nombre", "Agregar Producto", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtCantidad.getText().isEmpty() == true){
            JOptionPane.showMessageDialog(null, "Por favot ingrese un cantidad", "Agregar Producto", JOptionPane.INFORMATION_MESSAGE);
        }else{
        Productos registro = new Productos();
        registro.setNombreProducto(txtNombreProducto.getText());
        registro.setCantidad(Integer.parseInt(txtCantidad.getText()));

            try{
                 PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarProducto(?,?)}");
                 procedimiento.setString(1,registro.getNombreProducto());
                 procedimiento.setInt(2,registro.getCantidad());
                 procedimiento.execute();
                 ListaProductos.add(registro);
                 cargarDatos();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    public void seleccionarElemento(){
        txtCodigoProducto.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
        txtNombreProducto.setText(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getNombreProducto());
         txtCantidad.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getCantidad()));
    }
    public Productos BuscarProductos( int codigoProducto){
        Productos resultado = null;
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarProducto(?)}");
                procedimiento.setInt(1,codigoProducto);
                ResultSet registro = procedimiento.executeQuery();
            while (registro.next()){
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
    public void eliminar() {
        switch (tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnNuevo.setDisable(false);
                btnEliminar.setText("Eliminar");
                btnEliminar.setDisable(false);
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if(tblProductos.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Desea eliminar el registro?","Eliminar Producto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION)
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ElimnarProductos(?)}");
                             procedimiento.setInt(1, ((Productos)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
                             ListaProductos.remove(tblProductos.getSelectionModel().getFocusedIndex());
                             limpiarControles();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                }else{
                   JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
        }
    }
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
               if(tblProductos.getSelectionModel().getSelectedItem() !=null){
                 btnEditar.setText("actualizar");
                btnReporte.setText("Cancelar");
                btnNuevo.setDisable(true);
                btnEliminar.setDisable(true);
                activarControles();
                limpiarControles();
                tipoDeOperacion = operaciones.ACTUALIZAR;
               }else{
                   JOptionPane.showMessageDialog(null,"debe seleccionar un elemento");
               }
               break;
            case ACTUALIZAR:
                actualizar();
                    btnEditar.setText("Editar");
                    btnReporte.setText("reporte");
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    tipoDeOperacion = operaciones.NINGUNO;
                    cargarDatos();
                break;
        }
    }
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarProductos(?,?,?)}");
            Productos registro = (Productos)tblProductos.getSelectionModel().getSelectedItem();
            registro.setNombreProducto(txtNombreProducto.getText());
            registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
            procedimiento.setInt(1,registro.getCodigoProducto());
            procedimiento.setString(2,registro.getNombreProducto());
            procedimiento.setInt(3,registro.getCantidad());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
               
    }
    public void desactivarControles(){
        txtCodigoProducto.setEditable(false);
        txtNombreProducto.setEditable(false);
        txtCantidad.setEditable(false);
    }
    public void activarControles(){
        txtCodigoProducto.setEditable(false);
        txtNombreProducto.setEditable(true);
        txtCantidad.setEditable(true);
    }
    public void limpiarControles(){
       txtCodigoProducto.setText("");
        txtNombreProducto.setText("");
        txtCantidad.setText("");
    }
     

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
}

