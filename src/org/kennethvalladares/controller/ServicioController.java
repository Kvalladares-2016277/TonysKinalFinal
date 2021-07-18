
package org.kennethvalladares.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
import org.kennethvalladares.bean.Empresa;
import org.kennethvalladares.bean.Servicio;
import org.kennethvalladares.db.Conexion;
import org.kennethvalladares.report.GenerarReporte;
import org.kennethvalladares.system.Principal;

/**
 *
 * @author luis valladares
 */
public class ServicioController implements Initializable {
         private enum operaciones {NUEVO,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
         private operaciones tipoDeOperacion = operaciones.NINGUNO;
         private Principal escenarioPrincipal;
         private ObservableList<Servicio> listaServicio;
         private ObservableList<Empresa> listaEmpresa;
        private DatePicker fecha;
         
         @FXML private TableView tblServicios;
         @FXML private TableColumn colCodigoPresupuesto;
         @FXML private TableColumn colFechaServicio;
         @FXML private TableColumn colTipoServicio;
         @FXML private TableColumn colHoraServicio;
         @FXML private TableColumn colLugarServicio;
         @FXML private TableColumn colContacto;
         @FXML private TableColumn colCodigoEmpresa;
         
         @FXML private TextField txtCodigoServicio;
         @FXML private TextField txtTipoServicio;
         @FXML private TextField txtHoraServicio;
         @FXML private TextField txtLugarServicio;
         @FXML private TextField txtTelefonoServicio;
         
         @FXML private ComboBox cmbCodigoEmpresa;
         @FXML private GridPane grpFechaServicio;
         
         
         @FXML private Button btnNuevo;
         @FXML private Button btnEliminar;
         @FXML private Button btnEditar;
         @FXML private Button btnReporte;
         
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
       fecha = new  DatePicker(Locale.ENGLISH);
      fecha.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
      fecha.getCalendarView().todayButtonTextProperty().set("Today");
      fecha.getCalendarView().setShowWeeks(false);
      fecha.getStylesheets().add("/org/kennethvalladares/resource/DatePicker.css");
      grpFechaServicio.add(fecha,0,0);
      cmbCodigoEmpresa.setItems(getEmpresa());
      
    }
    
     public void cargarDatos(){
        tblServicios.setItems(getServicio());
        colCodigoPresupuesto.setCellValueFactory(new PropertyValueFactory<Servicio, Integer>("codigoServicio"));
        colFechaServicio.setCellValueFactory(new PropertyValueFactory<Servicio, Date>("fechaServicio"));
        colTipoServicio.setCellValueFactory(new PropertyValueFactory<Servicio, String>("tipoServicio"));
        colHoraServicio.setCellValueFactory(new PropertyValueFactory<Servicio, String>("horaServicio"));
        colLugarServicio.setCellValueFactory(new PropertyValueFactory<Servicio, String>("lugarServicio"));
        colContacto.setCellValueFactory(new PropertyValueFactory<Servicio, String>("telefonoContacto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Servicio, Integer>("codigoEmpresa"));
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
    public ObservableList<Empresa> getEmpresa(){
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpresa}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Empresa( resultado.getInt("codigoEmpresa"),
                                        resultado.getString("nombreEmpresa"),
                                        resultado.getString("direccion"),
                                        resultado.getString("telefono")));
            }
        }catch(Exception e){
            e.printStackTrace();
    }
        return listaEmpresa = FXCollections.observableArrayList(lista);
    }
    public Empresa buscarEmpresa (int codigoEmpresa){
        Empresa resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpresa(?)}");
            procedimiento.setInt(1, codigoEmpresa);
            ResultSet registro = procedimiento.executeQuery();
                while(registro.next()){
                    resultado = new Empresa(registro.getInt("codigoEmpresa"),
                                    registro.getString("nombreEmpresa"),
                                    registro.getString("direccion"),
                                    registro.getString("telefono"));
                }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    public void seleccionarElemento(){
        txtCodigoServicio.setText(String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio()));
        txtTipoServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTipoServicio());
        txtHoraServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getHoraServicio());
        txtLugarServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getLugarServicio());
        txtTelefonoServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTelefonoContacto());
        fecha.selectedDateProperty().set((((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getFechaServicio()));
        cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
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
       if(fecha.getSelectedDate() == null){
            JOptionPane.showMessageDialog(null, "Por favor ingrese una fecha", "Agregar Servicio", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtTipoServicio.getText().isEmpty() == true){
            JOptionPane.showMessageDialog(null, "Por favor ingrese un Tipo", "Agregar Servicio", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtHoraServicio.getText().isEmpty() == true){
            JOptionPane.showMessageDialog(null, "Por favor ingrese una Hora", "Agregar Servicio", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtHoraServicio.getText().matches(".*.*:.*.*:.*.*") == false){
            JOptionPane.showMessageDialog(null, "Por favor ingrese el formato debido de la hora Ejemplo: HH:MM:SS", "Agregar Servicio", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtLugarServicio.getText().isEmpty() == true){
            JOptionPane.showMessageDialog(null, "Por favor ingrese un Lugar", "Agregar Servicio", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtTelefonoServicio.getText().isEmpty() == true){
            JOptionPane.showMessageDialog(null, "Por favor ingrese un numero de Telefono", "Agregar Servicio", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtTelefonoServicio.getText().matches(".*.*.*.*.*.*.*.*") == false){
            JOptionPane.showMessageDialog(null, "Por favor ingrese un numero real", "Agregar Servicio", JOptionPane.INFORMATION_MESSAGE);
        }else if(cmbCodigoEmpresa.getSelectionModel().getSelectedItem() == null){
            JOptionPane.showMessageDialog(null, "Por favor elija un codigo de Empresa", "Agregar Servicio", JOptionPane.INFORMATION_MESSAGE);
        }else{
       Servicio registro = new Servicio();
       registro.setFechaServicio(fecha.getSelectedDate());
       registro.setTipoServicio(txtTipoServicio.getText());
       registro.setHoraServicio(txtHoraServicio.getText());
       registro.setLugarServicio(txtLugarServicio.getText());
       registro.setTelefonoContacto(txtTelefonoServicio.getText());
       registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarServicio(?,?,?,?,?,?)}");
            procedimiento.setDate(1 , new java.sql.Date(registro.getFechaServicio().getTime()));
            procedimiento.setString(2,registro.getTipoServicio());
            procedimiento.setString(3,registro.getHoraServicio());
            procedimiento.setString(4,registro.getLugarServicio());
            procedimiento.setString(5,registro.getTelefonoContacto());
            procedimiento.setInt(6, registro.getCodigoEmpresa());
            procedimiento.execute();
            listaServicio.add(registro);
                    }catch(Exception e){
            e.printStackTrace();
        }
       }
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
               break;
            default:
                    if(tblServicios.getSelectionModel().getSelectedItem() !=null){
                        int respuesta = JOptionPane.showConfirmDialog(null,"¿Deseas eliminar este registro?","Eliminar Servicio",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                           try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicio(?)}");
                            procedimiento.setInt(1, ((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio());
                            procedimiento.execute();
                            listaServicio.remove(tblServicios.getSelectionModel().getSelectedItem());
                            limpiarControles();
                        }catch(Exception e){
                            e.printStackTrace();
                        } 
                    }else{
                            if(respuesta == JOptionPane.NO_OPTION){
                            limpiarControles();
                            cargarDatos();
                        }                    
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debes seleccionar un elemento!"); 
                        }
        }
        
        } 
    public void editar(){
            switch(tipoDeOperacion){
                case NINGUNO:
                        if(tblServicios.getSelectionModel().getSelectedItem() !=null){
                            activarControles();
                            limpiarControles();
                            btnNuevo.setDisable(true);
                            btnEliminar.setDisable(true);
                            btnEditar.setText("Actualizar");
                            btnReporte.setText("Cancelar");
                            tipoDeOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debes seleccionar un elemento!");
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarServicio(?,?,?,?,?,?,?)}");
            Servicio registro = (Servicio)tblServicios.getSelectionModel().getSelectedItem();
            registro.setFechaServicio(fecha.getSelectedDate());
            registro.setTipoServicio(txtTipoServicio.getText());
            registro.setHoraServicio(txtHoraServicio.getText());
            registro.setLugarServicio(txtLugarServicio.getText());
            registro.setTelefonoContacto(txtTelefonoServicio.getText());
            registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
            
            procedimiento.setInt(1, registro.getCodigoEmpresa());
            procedimiento.setDate(2,new java.sql.Date(registro.getFechaServicio().getTime()));
            procedimiento.setString(3,registro.getTipoServicio());
            procedimiento.setString(4,registro.getHoraServicio());
            procedimiento.setString(5, registro.getLugarServicio());
            procedimiento.setString(6, registro.getTelefonoContacto());
            procedimiento.setInt(7, registro.getCodigoEmpresa());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
     public void generarReporte(){
            switch(tipoDeOperacion){
                case NINGUNO:
                if(tblServicios.getSelectionModel().getSelectedItem() != null){
                imprimirReporte();
                }else{
                    JOptionPane.showMessageDialog(null,"Debe seleccionar una fila de datos para generar un reporte");
                            
                }
                break;
                    
        }
       }
public void imprimirReporte(){
Map parametros = new HashMap();
int codigoServicio = Integer.valueOf(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
parametros.put("codigoServicio",codigoServicio);
GenerarReporte.mostarReporte("ReporteServicio.jasper","Reporte Servicio",parametros);

 

}

    public void desactivarControles(){
        txtCodigoServicio.setEditable(false);
        txtTipoServicio.setEditable(false);
        txtHoraServicio .setEditable(false);
        txtLugarServicio.setEditable(false);
        txtTelefonoServicio.setEditable(false);
        grpFechaServicio.setDisable(false);
        cmbCodigoEmpresa.setDisable(false);
    }
    public void activarControles(){
        txtCodigoServicio.setEditable(false);
        txtTipoServicio.setEditable(true);
        txtHoraServicio .setEditable(true);
        txtLugarServicio.setEditable(true);
        txtTelefonoServicio.setEditable(true);
        grpFechaServicio.setDisable(false);
        cmbCodigoEmpresa.setEditable(false);
    }
    public void limpiarControles(){
        txtCodigoServicio.setText("");
        txtTipoServicio.setText("");
        txtHoraServicio.setText("");
        txtLugarServicio.setText("");
        txtTelefonoServicio.setText("");
        cmbCodigoEmpresa.getSelectionModel().clearSelection();
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
    public void VentanaEmpresas(){
        escenarioPrincipal.VentanaEmpresas();
    }
    public void VentanaServicio(){
        escenarioPrincipal.VentanaServicio();
    }
}
