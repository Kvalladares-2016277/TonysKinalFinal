
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
import org.kennethvalladares.bean.TipoEmpleado;
import org.kennethvalladares.db.Conexion;
import org.kennethvalladares.system.Principal;

/**
 *
 * @author luis valladares
 */
public class TipoEmpleadoController implements Initializable {
    private enum operaciones {NUEVO,GUARDAR,ELIMIAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<TipoEmpleado>ListaTipoEmpleado;
    
       
   @FXML private TextField txtCodigoTipoEmpleado;
   @FXML private TextField txtDescripcionTipoEmpleado;
   @FXML private TableView tblTipoEmpleado;
   @FXML private TableColumn colCodigoEmpresa;
   @FXML private TableColumn colDescripcionTipoEmpleado;
   @FXML private Button btnNuevo;
   @FXML private Button btnEliminar;
   @FXML private Button btnEditar;
   @FXML private Button btnReporte;
   
    @Override
    public void initialize(URL location, ResourceBundle resources) {
            cargarDatos();
    } 
        public void cargarDatos(){
           tblTipoEmpleado.setItems(getTipoEmpleado());
           colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<TipoEmpleado,Integer>("codigoTipoEmpleado"));
           colDescripcionTipoEmpleado.setCellValueFactory(new PropertyValueFactory<TipoEmpleado,String>("descripcion"));
    }
        public ObservableList<TipoEmpleado>getTipoEmpleado(){
           ArrayList<TipoEmpleado> lista = new ArrayList<TipoEmpleado>();
           try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoEmpleado}");
                  ResultSet resultado = procedimiento.executeQuery();
         while(resultado.next()){
                      lista.add(new TipoEmpleado (resultado.getInt("codigoTipoEmpleado"),
                                        resultado.getString("descripcion")));
                                       
              }
           }catch(Exception e){
               e.printStackTrace();
           }
           return ListaTipoEmpleado = FXCollections.observableArrayList(lista);
        }
       public void nuevo(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                    activarControles();
                    limpiarControles();
                        btnNuevo.setText("Guardar");
                        btnEliminar.setDisable(false);
                        btnEliminar.setText("Cancelar");
                        btnEditar.setDisable(true);
                        btnReporte.setDisable(true);
                        tipoDeOperaciones = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                    desactivarControles();
                    limpiarControles();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    btnEliminar.setDisable(true);
                    btnEditar.setDisable(true);
                    btnReporte.setDisable(true);
                    tipoDeOperaciones = operaciones.NINGUNO;
               break;
        }   
       
            }
             
        public void guardar(){
            if(txtDescripcionTipoEmpleado.getText().isEmpty() == true){
            JOptionPane.showMessageDialog(null, "Por favor ingrese una descripcion", "Agregar Tipo de Empleado", JOptionPane.INFORMATION_MESSAGE);
            }else{
            TipoEmpleado registro = new TipoEmpleado();
            registro.setDescripcion(txtDescripcionTipoEmpleado.getText());
               try{
                   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarTipoEmpleado(?)}");
                   procedimiento.setString(1,registro.getDescripcion());
                   procedimiento.execute();
                   ListaTipoEmpleado.add(registro);
                   cargarDatos();
               }catch(Exception e){
                   e.printStackTrace();
               }
            }
        }
        
        
    public void seleccionarElemento(){
    txtCodigoTipoEmpleado.setText(String.valueOf(((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
     txtDescripcionTipoEmpleado.setText(((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getDescripcion());
    }
    public TipoEmpleado buscarTipoEmpleado (int codigoTipoEmpleado){
      TipoEmpleado resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoEmpleado(?}");
            procedimiento.setInt(1,codigoTipoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
                while(registro.next()){
                    resultado = new TipoEmpleado(
                            registro.getInt("codigoTipoEmpleado"),
                            registro.getString("descripcion"));
                }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }

    public void eliminar(){
        switch(tipoDeOperaciones){
            case GUARDAR:
                   desactivarControles();
                   limpiarControles();
                   btnNuevo.setText("Nuevo"); 
                   btnNuevo.setDisable(false);
                   btnEliminar.setText("Eliminar");
                   btnEliminar.setDisable(false);
                   btnEditar.setDisable(false);
                   btnReporte.setDisable(false);
                   tipoDeOperaciones = operaciones.NINGUNO;
                 break;
            default:
                 if(tblTipoEmpleado.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta Seguro De Eliminar los Datos?","Eliminar TipoEmpleado",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                      if(respuesta == JOptionPane.YES_OPTION){
                          try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarTipoEmpleado(?)}");
                            procedimiento.setInt(1 ,((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
                            procedimiento.execute();
                            ListaTipoEmpleado.remove(tblTipoEmpleado.getSelectionModel().getFocusedIndex());
                            limpiarControles();
                   
                          }catch(Exception e){
                              e.printStackTrace();
                          }
                 }else{
                          JOptionPane.showMessageDialog(null,"Debe seleccionar un elemento");
                      }
               }        
        }
    }
    public void editar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                    if(tblTipoEmpleado.getSelectionModel().getSelectedItem() !=null){
                btnEditar.setText("actualizar");
                btnReporte.setText("Cancelar");
                btnNuevo.setDisable(true);
                btnEliminar.setDisable(true);
                activarControles();
                limpiarControles();
                tipoDeOperaciones = operaciones.ACTUALIZAR;
            }else{
                    JOptionPane.showMessageDialog(null,"Debe seleccionar un elemento");
             }
            break;
            case ACTUALIZAR:
                actualizar();
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    tipoDeOperaciones = operaciones.NINGUNO;
                    cargarDatos();
                    break;
        }
    }  
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarTipoEmpleado(?,?)}");
            TipoEmpleado registro = (TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtDescripcionTipoEmpleado.getText());
            procedimiento.setInt(1,registro.getCodigoTipoEmpleado());
            procedimiento.setString(2,registro.getDescripcion());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
     public void reporte(){
        switch (tipoDeOperaciones){
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnReporte.setText("Reporte");
                btnEditar.setText("Editar");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setDisable(false);
                tipoDeOperaciones = operaciones.NINGUNO;
                break;  
        }
    }
    
    public void desactivarControles(){
        txtCodigoTipoEmpleado.setEditable(false);
        txtDescripcionTipoEmpleado.setEditable(false);
    }
    public void activarControles(){
        txtCodigoTipoEmpleado.setEditable(false);
        txtDescripcionTipoEmpleado.setEditable(true);
    }
    public void limpiarControles(){
        txtCodigoTipoEmpleado.setText("");
        txtDescripcionTipoEmpleado.setText("");
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
     public void VentanaEmpleado(){
         escenarioPrincipal.VentanaEmpleado();
     }
}
