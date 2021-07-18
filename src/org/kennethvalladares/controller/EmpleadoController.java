
package org.kennethvalladares.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.kennethvalladares.bean.Empleado;
import org.kennethvalladares.bean.TipoEmpleado;
import org.kennethvalladares.db.Conexion;
import org.kennethvalladares.system.Principal;

/**
 *
 * @author luis valladares
 */
public class EmpleadoController implements Initializable {
    private enum operaciones {NUEVO,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Empleado> listaEmpleado;
    private ObservableList<TipoEmpleado> listaTipoEmpleado;
    
    @FXML private TextField txtCodigoEmpleado;
    @FXML private TextField txtNumeroEmpleado;
    @FXML private TextField txtApellidosEmpleado;
    @FXML private TextField txtNombreEmpleado;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtGradoCocinero;
    @FXML private ComboBox cmbCodigoTipoEmpleado;
    @FXML private TableView tblEmpleados;
    @FXML private TableColumn colCodigoEmpleado;
    @FXML private TableColumn colNumeroEmpleado;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colTelefono;
    @FXML private TableColumn colGradoCocinero;
    @FXML private TableColumn colCodigoTipoEmpleado;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        
       cmbCodigoTipoEmpleado.setItems(getTipoEmpleado()); 
    }
    
    public void cargarDatos(){
        tblEmpleados.setItems(getEmpleado());
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoEmpleado"));
        colNumeroEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("numeroEmpleado"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Empleado, String>("apellidosEmpleado"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombresEmpleado"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Empleado, String>("direccionEmpleado"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Empleado, String>("telefonoContacto"));
        colGradoCocinero.setCellValueFactory(new PropertyValueFactory<Empleado, String>("gradoCocinero"));
        colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoTipoEmpleado"));
    }
    
    public ObservableList<Empleado> getEmpleado(){
        ArrayList<Empleado> lista = new ArrayList<Empleado>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpleado}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Empleado(resultado.getInt("codigoEmpleado"), 
                                       resultado.getInt("numeroEmpleado"), 
                                       resultado.getString("apellidosEmpleado"), 
                                       resultado.getString("nombresEmpleado"), 
                                       resultado.getString("direccionEmpleado"), 
                                       resultado.getString("telefonoContacto"), 
                                       resultado.getString("gradoCocinero"), 
                                       resultado.getInt("codigoTipoEmpleado")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEmpleado = FXCollections.observableArrayList(lista);
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
           return listaTipoEmpleado = FXCollections.observableArrayList(lista);
                   
    }
    public void seleccionarDatos(){
        txtCodigoEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
        txtNumeroEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getNumeroEmpleado()));
        txtApellidosEmpleado.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getApellidosEmpleado());
        txtNombreEmpleado.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getNombresEmpleado());
        txtDireccion.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getDireccionEmpleado());
        txtTelefono.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getTelefonoContacto());
        txtGradoCocinero.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getGradoCocinero());
        cmbCodigoTipoEmpleado.getSelectionModel().select(buscarTipoEmpelado(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
        
    }

    public TipoEmpleado buscarTipoEmpelado (int codigoTipoEmpleado){
        TipoEmpleado resultado = null;
            try{
               PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoEmpleado(?)}");
               procedimiento.setInt(1, codigoTipoEmpleado);
               ResultSet registro = procedimiento.executeQuery();
                while(registro.next()){
                    resultado = new TipoEmpleado ( registro.getInt("codigoTipoEmpleado"),
                            registro.getString("descripcion"));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        return resultado;
    }
    
   public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
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
                btnEliminar.setText("Eliminar");
                btnEliminar.setDisable(false);
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
   public void guardar(){
       if(txtNumeroEmpleado.getText().isEmpty() == true){
            JOptionPane.showMessageDialog(null, "Por favor ingrese un numero","Agregar Empleado", JOptionPane.INFORMATION_MESSAGE);
        }else if (txtApellidosEmpleado.getText().isEmpty() == true){
            JOptionPane.showMessageDialog(null, "Por favor ingrese sus apellidos", "Agregar Empleado", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtNombreEmpleado.getText().isEmpty() == true){
            JOptionPane.showMessageDialog(null, "Por favot ingrese su nombre","Agregar Empleado", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtDireccion.getText().isEmpty() == true){
            JOptionPane.showMessageDialog(null, "Por favot ingrese una direccion","Agregar Empleado", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtTelefono.getText().isEmpty() == true){
            JOptionPane.showMessageDialog(null, "Por favor ingrese su numero de telefono","Agregar Empleado",JOptionPane.INFORMATION_MESSAGE);
        }else if(txtGradoCocinero.getText().isEmpty() == true){
            JOptionPane.showMessageDialog(null, "Por favor ingrese su nivel de grado", "Agregar Empleado", JOptionPane.INFORMATION_MESSAGE);
        }else if(cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem() == null){
            JOptionPane.showMessageDialog(null, "Por favor escoja un codigo de tipo empleado", "Agregar Empleado", JOptionPane.INFORMATION_MESSAGE);
        }else{
       Empleado registro = new Empleado();
       registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
       registro.setApellidosEmpleado(txtApellidosEmpleado.getText());
       registro.setNombresEmpleado(txtNombreEmpleado.getText());
       registro.setDireccionEmpleado(txtDireccion.getText());
       registro.setTelefonoContacto(txtTelefono.getText());
       registro.setGradoCocinero(txtGradoCocinero.getText());
       registro.setCodigoTipoEmpleado(((TipoEmpleado)this.cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
       try{
       PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpleado(?,?,?,?,?,?,?)}");
        procedimiento.setInt(1, registro.getNumeroEmpleado());
           procedimiento.setString(2, registro.getApellidosEmpleado());
           procedimiento.setString(3, registro.getNombresEmpleado());
           procedimiento.setString(4, registro.getDireccionEmpleado());
           procedimiento.setString(5, registro.getTelefonoContacto());
           procedimiento.setString(6, registro.getGradoCocinero());
           procedimiento.setInt(7, registro.getCodigoTipoEmpleado());
           procedimiento.execute();
        listaEmpleado.add(registro);
       }catch(Exception e){
           e.printStackTrace();
       }
     }
   }   
     public TipoEmpleado buscarEmpresa (int codigoTipoEmpleado){
         TipoEmpleado resultado = null;
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoEmpleado(?)}");
                procedimiento.setInt(1, codigoTipoEmpleado);
                ResultSet registro = procedimiento.executeQuery();
                    while(registro.next()){
                        resultado = new TipoEmpleado(registro.getInt("codigoTipoEmpleado"),
                                    registro.getString("descripcion"));
                    
                    }
            }catch(Exception e){
                e.printStackTrace();
            }
         
         
         return resultado;
     }     
    public Empleado buscarEmpleado(int codigoEmpleado){
        Empleado resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpleado(?)}");
            procedimiento.setInt(1, codigoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()){
                resultado = new Empleado(
                                    registro.getInt("codigoEmpleado"),
                                    registro.getInt("numeroEmpleado"),
                                    registro.getString("apellidosEmpleado"),
                                    registro.getString("nombresEmpleado"),
                                    registro.getString("direccionEmpleado"),
                                    registro.getString("telefonoContacto"),
                                    registro.getString("gradoCocinero"),
                                    registro.getInt("codigoTipoEmpleado")                        
                                );
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
              if(tblEmpleados.getSelectionModel().getSelectedItem() !=null){
                  int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro de eliminar el registro?","eliminar Empleado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(respuesta == JOptionPane.YES_OPTION) 
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEmpleado(?)}");
                            procedimiento.setInt(1, ((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                            procedimiento.execute();
                            listaEmpleado.remove(tblEmpleados.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
              }else{
                  JOptionPane.showMessageDialog(null,"Debe seleccionar un dato");
              }    
        }
    }
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblEmpleados.getSelectionModel().getSelectedItem() != null){
                   btnEditar.setText("Actualizar");
                   btnReporte.setText("Cancelar");
                   btnNuevo.setDisable(true);
                   btnEliminar.setDisable(true);
                   activarControles();
                   limpiarControles();
                   tipoDeOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
                break;
                case ACTUALIZAR:
                    actualizar();
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    tipoDeOperacion = operaciones.NINGUNO;
                    cargarDatos();
                    break;
        }
    }
     public void reporte(){
        switch (tipoDeOperacion){
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnReporte.setText("Reporte");
                btnEditar.setText("Editar");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;  
        }
    }

    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarEmpleado(?,?,?,?,?,?,?,?)}");
            Empleado registro = (Empleado)tblEmpleados.getSelectionModel().getSelectedItem();
            registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
            registro.setApellidosEmpleado(txtApellidosEmpleado.getText());
            registro.setNombresEmpleado(txtNombreEmpleado.getText());
            registro.setDireccionEmpleado(txtDireccion.getText());
            registro.setTelefonoContacto(txtTelefono.getText());
            registro.setGradoCocinero(txtGradoCocinero.getText());
            
            procedimiento.setInt(1, registro.getCodigoEmpleado());
            procedimiento.setInt(2,registro.getNumeroEmpleado());
            procedimiento.setString(3,registro.getApellidosEmpleado());
            procedimiento.setString(4,registro.getNombresEmpleado());
            procedimiento.setString(5,registro.getDireccionEmpleado());
            procedimiento.setString(6,registro.getTelefonoContacto());
            procedimiento.setString(7,registro.getGradoCocinero());
            procedimiento.setInt(8,registro.getCodigoTipoEmpleado());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
   
    
    
    
        public void desactivarControles(){
        txtCodigoEmpleado.setEditable(false);
        txtNumeroEmpleado.setEditable(false);
        txtApellidosEmpleado.setEditable(false);
        txtNombreEmpleado.setEditable(false);
        txtDireccion.setEditable(false);
        txtTelefono.setEditable(false);
        txtGradoCocinero.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoEmpleado.setEditable(false);
        txtNumeroEmpleado.setEditable(true);
        txtApellidosEmpleado.setEditable(true);
        txtNombreEmpleado.setEditable(true);
        txtDireccion.setEditable(true);
        txtTelefono.setEditable(true);
        txtGradoCocinero.setEditable(true);        
    }
    
    public void limpiarControles(){
        txtCodigoEmpleado.setText("");
        txtNumeroEmpleado.setText("");
        txtApellidosEmpleado.setText("");
        txtNombreEmpleado.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtGradoCocinero.setText("");
        cmbCodigoTipoEmpleado.getSelectionModel().clearSelection();
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
    public void VentanaTipoEmpleado(){
        escenarioPrincipal.VentanaTipoEmpleado();
    }
}
