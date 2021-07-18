
package org.kennethvalladares.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.kennethvalladares.bean.Empleado;
import org.kennethvalladares.bean.Servicio;
import org.kennethvalladares.bean.Servicio_has_Empleado;
import org.kennethvalladares.db.Conexion;
import org.kennethvalladares.system.Principal;

/**
 *
 * @author luis valladares
 */
public class Servicios_has_EmpleadosController implements Initializable{
 private enum operaciones {NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Servicio_has_Empleado> listaServicio_has_Empleado;
    private ObservableList<Servicio> listaServicio;
    private ObservableList<Empleado> listaEmpleado;
     private DatePicker fecha;
    
    @FXML private TableView tblServicios_Empleados;
    @FXML private TableColumn colServicios_codigoServicio;
    @FXML private TableColumn colEmpleados_codigoEmpleado;
    @FXML private TableColumn colfechaEvento;
    @FXML private TableColumn colHoraEvento;
    @FXML private TableColumn colLugarEvento;
     
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    @FXML private ComboBox cmbServicioCodigoServicio;
    @FXML private ComboBox cmbEmpleadoCodigoEmpleado;
    @FXML private GridPane dtpFechaServicioEmpleado;
    
    @FXML private TextField txtHoraEvento;
    @FXML private  TextField txtLugarEvento;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("YYYY-MM-DD"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/kennethvalladares/resource/DatePicker.css");
        dtpFechaServicioEmpleado.add(fecha, 0, 0);
        this.cmbEmpleadoCodigoEmpleado.setItems(getEmpleado());
        this.cmbServicioCodigoServicio.setItems(getServicio());
      }   
    
    public void cargarDatos(){
        tblServicios_Empleados.setItems(getServicioEmpleado());
        colServicios_codigoServicio.setCellValueFactory(new PropertyValueFactory<Servicio_has_Empleado, Integer>("codigoServicio"));
        colEmpleados_codigoEmpleado.setCellValueFactory(new PropertyValueFactory<Servicio_has_Empleado, Integer>("codigoEmpleado"));
        colfechaEvento.setCellValueFactory(new PropertyValueFactory<Servicio_has_Empleado, Date>("fechaEvento"));
        colHoraEvento.setCellValueFactory(new PropertyValueFactory<Servicio_has_Empleado, String>("horaEvento"));
        colLugarEvento.setCellValueFactory(new PropertyValueFactory<Servicio_has_Empleado, String>("lugarEvento"));
    }
    
    public ObservableList<Servicio_has_Empleado> getServicioEmpleado(){
        ArrayList<Servicio_has_Empleado> lista = new ArrayList<Servicio_has_Empleado>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServiciosEmpleados}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicio_has_Empleado(resultado.getInt("Servicios_codigoServicio"), 
                                                    resultado.getInt("Empleados_codigoEmpleado"), 
                                                    resultado.getDate("fechaEvento"), 
                                                    resultado.getString("horaEvento"), 
                                                    resultado.getString("lugarEvento")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaServicio_has_Empleado = FXCollections.observableArrayList(lista);
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
    public void seleccionarElemento(){
        cmbServicioCodigoServicio.getSelectionModel().select(buscarServicio(((Servicio_has_Empleado)tblServicios_Empleados.getSelectionModel().getSelectedItem()).getCodigoServicio()));
        cmbEmpleadoCodigoEmpleado.getSelectionModel().select(buscarEmpleado(((Servicio_has_Empleado)tblServicios_Empleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
        fecha.selectedDateProperty().set(((Servicio_has_Empleado)tblServicios_Empleados.getSelectionModel().getSelectedItem()).getFechaEvento());
        txtHoraEvento.setText(((Servicio_has_Empleado)tblServicios_Empleados.getSelectionModel().getSelectedItem()).getHoraEvento());
        txtLugarEvento.setText(((Servicio_has_Empleado)tblServicios_Empleados.getSelectionModel().getSelectedItem()).getLugarEvento());
    }
        //CRUD DE LA ENTIDAD 
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
    //metodo guardar
        public void guardar(){
         if(cmbServicioCodigoServicio.getSelectionModel().getSelectedItem() == null){
            JOptionPane.showMessageDialog(null, "Por favor elija un Codigo de Empleado", "Modificar Servicio y Empleado", JOptionPane.INFORMATION_MESSAGE);
        }else if(cmbEmpleadoCodigoEmpleado.getSelectionModel().getSelectedItem() == null){
            JOptionPane.showMessageDialog(null, "Por favor elija un Codigo de Empleado", "Modificar Servicio y Empleado", JOptionPane.INFORMATION_MESSAGE);
        }else if(fecha.getSelectedDate() == null){
            JOptionPane.showMessageDialog(null, "Por favor ingrese un fecha", "Modificar Servicio y Empleado", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtHoraEvento.getText().isEmpty() == true){
            JOptionPane.showMessageDialog(null, "Por favor ingrese una Hora", "Modificar Servicio y Empleado", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtLugarEvento.getText().isEmpty() == true){
            JOptionPane.showMessageDialog(null, "Por favor ingrese un Lugar", "Modificar Servicio y Empleado", JOptionPane.INFORMATION_MESSAGE);
        }else{
         Servicio_has_Empleado registro = new Servicio_has_Empleado();
         registro.setCodigoServicio(((Servicio)cmbServicioCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
         registro.setCodigoEmpleado(((Empleado)cmbEmpleadoCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
         registro.setFechaEvento(fecha.getSelectedDate());
         registro.setHoraEvento(txtHoraEvento.getText());
         registro.setLugarEvento(txtLugarEvento.getText());
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarServiciosEmpleados(?,?,?,?,?)}");
                procedimiento.setInt(1,registro.getCodigoServicio());
                procedimiento.setInt(2,registro.getCodigoEmpleado());
                procedimiento.setDate(3, new java.sql.Date(registro.getFechaEvento().getTime()));
                procedimiento.setString(4,registro.getHoraEvento());
                procedimiento.setString(5,registro.getLugarEvento());
                procedimiento.execute();
                listaServicio_has_Empleado.add(registro);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        }
        
        public void eliminar(){
            switch(tipoDeOperacion){
                case GUARDAR:
                    desactivarControles();
                    limpiarControles();
                    btnNuevo.setText("Nuevo");
                    btnNuevo.setDisable(false);
                    btnEliminar.setText("Eliminar");
                    btnEliminar.setDisable(false);
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    break;
                default:
                    if(tblServicios_Empleados.getSelectionModel().getSelectedItem() == null){
                        JOptionPane.showMessageDialog(null, "Por favor seleccione un dato para eliminar", "Eliminar Servicios_Has_Empleados", JOptionPane.ERROR_MESSAGE);
                    }else{
                        int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar este dato?", "Eliminar Servicios_Has_Empleados", JOptionPane.YES_NO_OPTION);
                        if(respuesta == JOptionPane.YES_OPTION){
                            try{
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServiciosEmpleados(?)}");
                                procedimiento.setInt(1, ((Servicio_has_Empleado)tblServicios_Empleados.getSelectionModel().getSelectedItem()).getCodigoServicio());
                                procedimiento.execute();
                                listaServicio_has_Empleado.remove(tblServicios_Empleados.getSelectionModel().getFocusedIndex());
                            }catch(SQLException e){
                                if(e.getErrorCode() == 1146){
                                    JOptionPane.showMessageDialog(null, "no se puede eliminar este dato ya que hay otro con el o los mismos ID", "Eliminar Servicios_Has_Empleados", JOptionPane.INFORMATION_MESSAGE);
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                                }
                            }
                        }
                    }
            }
        
        public void editar(){
            switch(tipoDeOperacion){
                case NINGUNO:
                    if(tblServicios_Empleados.getSelectionModel().getSelectedItem() != null){
                        activarControles();
                        limpiarControles();
                        btnNuevo.setDisable(true);
                        btnEliminar.setDisable(true);
                        btnEditar.setText("Actualizar");
                        btnReporte.setText("Cancelar");
                        tipoDeOperacion = operaciones.ACTUALIZAR;
                    }else{
                        JOptionPane.showMessageDialog(null,"debe seleccionar un elemento");
                    }
                    break;
                case ACTUALIZAR:
                    actualizar();
                    limpiarControles();
                    desactivarControles();
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    tipoDeOperacion = operaciones.NINGUNO;
                    cargarDatos();
                   break;
            }
        }
        
        public void actualizar(){
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarServiciosEmpleados(?,?,?,?,?)}");
                Servicio_has_Empleado registro = (Servicio_has_Empleado)tblServicios_Empleados.getSelectionModel().getSelectedItem();
                registro.setCodigoServicio(((Servicio)cmbServicioCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
                registro.setCodigoEmpleado(((Empleado)cmbEmpleadoCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                registro.setFechaEvento(fecha.getSelectedDate());
                registro.setHoraEvento(txtHoraEvento.getText());
                registro.setLugarEvento(txtLugarEvento.getText());
                procedimiento.setInt(1, registro.getCodigoServicio());
                procedimiento.setInt(2, registro.getCodigoEmpleado());
                procedimiento.setDate(3, new java.sql.Date(registro.getFechaEvento().getTime()));
                procedimiento.setString(4, registro.getHoraEvento());
                procedimiento.setString(5, registro.getLugarEvento());
                procedimiento.execute();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    //declaracion de objetos 
        public void desactivarControles(){
         cmbServicioCodigoServicio.setDisable(false);
         cmbEmpleadoCodigoEmpleado.setDisable(false);
         dtpFechaServicioEmpleado.setDisable(false);
         txtHoraEvento.setEditable(false);
         txtLugarEvento.setEditable(false);
        }
        public void activarControles(){
          cmbServicioCodigoServicio.setEditable(false);
          cmbEmpleadoCodigoEmpleado.setEditable(false);
          dtpFechaServicioEmpleado.setDisable(false);
          txtHoraEvento.setEditable(true);
          txtLugarEvento.setEditable(true);        
        }
        public void limpiarControles(){
          cmbServicioCodigoServicio.getSelectionModel().clearSelection();
          cmbEmpleadoCodigoEmpleado.getSelectionModel().clearSelection();
          txtHoraEvento.setText("");
          txtLugarEvento.setText("");
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
