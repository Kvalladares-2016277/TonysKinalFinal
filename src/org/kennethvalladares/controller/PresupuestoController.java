
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
import org.kennethvalladares.db.Conexion;
import org.kennethvalladares.system.Principal;
import org.kennethvalladares.bean.Empresa;
import org.kennethvalladares.bean.Presupuesto;
import org.kennethvalladares.report.GenerarReporte;


public class PresupuestoController implements Initializable {
   private enum operaciones {NUEVO,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
   private operaciones tipoDeOperacion = operaciones.NINGUNO;
   private Principal escenarioPrincipal;     
   private ObservableList<Presupuesto> listaPresupuesto;
   private ObservableList<Empresa>listarEmpresa;
   private DatePicker fecha;
   
   
   @FXML private TextField txtCodigoPresupuesto;
   @FXML private GridPane  grpFechaSolicitud;
   @FXML private TextField txtCantidadPresupuesto;
   @FXML private ComboBox  cmbCodigoEmpresa;
   @FXML private TableView tblPresupuesto;
   @FXML private TableColumn colCodigoPresupuesto;
   @FXML private TableColumn colFechaSolicitud;
   @FXML private TableColumn colCantidadPresupuesto;
   @FXML private TableColumn colCodigoEmpresa;
   @FXML private Button btnNuevo;
   @FXML private Button btnEliminar;
   @FXML private Button btnEditar;
   @FXML private Button btnReporte;
   
    @Override
    //metodo para crear objetos o se ejecuta 
    public void initialize(URL location, ResourceBundle resources) {
      cargarDatos();
      fecha = new  DatePicker(Locale.ENGLISH);
      fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
      fecha.getCalendarView().todayButtonTextProperty().set("Today");
      fecha.getCalendarView().setShowWeeks(false);
      fecha.getStylesheets().add("/org/kennethvalladares/resource/DatePicker.css");
      grpFechaSolicitud.add(fecha,0,0);
      cmbCodigoEmpresa.setItems(getEmpresa());
      
    }
    
    public void cargarDatos(){
        tblPresupuesto.setItems(getPresupuesto());
        colCodigoPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto, Integer>("codigoPresupuesto"));
        colFechaSolicitud.setCellValueFactory(new PropertyValueFactory<Presupuesto, Date>("fechaSolicitud"));
        colCantidadPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto, Float>("cantidadPresupuesto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Presupuesto, Integer>("codigoEmpresa"));
    }
    
    
    public ObservableList<Presupuesto> getPresupuesto(){
        ArrayList<Presupuesto> lista = new ArrayList<Presupuesto>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPresupuesto}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Presupuesto(resultado.getInt("codigoPresupuesto"), 
                                          resultado.getDate("fechaSolicitud"), 
                                          resultado.getFloat("cantidadPresupuesto"), 
                                          resultado.getInt("codigoEmpresa")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaPresupuesto = FXCollections.observableArrayList(lista);
    }
    
     ObservableList<Empresa> getEmpresa(){
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
            return listarEmpresa = FXCollections.observableArrayList(lista);
     }
     public void seleccionarElemento(){
       txtCodigoPresupuesto.setText(String.valueOf(((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getCodigoPresupuesto()));
       txtCantidadPresupuesto.setText(String.valueOf(((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getCantidadPresupuesto()));
       fecha.selectedDateProperty().set(((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getFechaSolicitud());
       cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
     }
     
     public Empresa buscarEmpresa (int codigoEmpresa){
        Empresa resultado = null;
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpresa(?)}");
                procedimiento.setInt(1,codigoEmpresa);
                ResultSet registro = procedimiento.executeQuery();
                    while (registro.next()){
                        resultado = new Empresa( registro.getInt("codigoEmpresa"),
                                 registro.getString("nombreEmpresa"),
                                 registro.getString("direccion"),
                                 registro.getString("telefono")         
                        );
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
        if(fecha.getSelectedDate() == null){
            JOptionPane.showMessageDialog(null, "Por favor elija una fecha", "Agregar Presupuesto", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtCantidadPresupuesto.getText().isEmpty() == true){
            JOptionPane.showMessageDialog(null, "Por favor ingrese una cantidad", "Agregar Presupuesto", JOptionPane.INFORMATION_MESSAGE);
        }else if(cmbCodigoEmpresa.getSelectionModel().getSelectedItem() == null){
            JOptionPane.showMessageDialog(null, "por favor elija un codigo de empresa", "Agregar Presupuesto", JOptionPane.INFORMATION_MESSAGE);
        }else{
        Presupuesto registro = new Presupuesto();
        registro.setFechaSolicitud(fecha.getSelectedDate());
        registro.setCantidadPresupuesto(Float.parseFloat(txtCantidadPresupuesto.getText()));
        registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarPresupuesto(?,?,?)}");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaSolicitud().getTime()));
            procedimiento.setDouble(2, registro.getCantidadPresupuesto());
            procedimiento.setInt(3, registro.getCodigoEmpresa());
            procedimiento.execute();
            listaPresupuesto.add(registro);
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
                if(tblPresupuesto.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(
                            null,"¿Deseas eliminar este registro?","Eliminar Presupuesto",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarPresupuesto(?)}");
                            procedimiento.setInt(1,((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getCodigoPresupuesto());
                            procedimiento.execute();
                            listaPresupuesto.remove(tblPresupuesto.getSelectionModel().getSelectedItem());
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
            break;
        }
    }
         public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblPresupuesto.getSelectionModel().getSelectedItem() != null){
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarPresupuesto(?,?,?,?)}");
            Presupuesto registro = (Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem();
            registro.setFechaSolicitud(fecha.getSelectedDate());
            registro.setCantidadPresupuesto(Float.parseFloat(txtCantidadPresupuesto.getText()));
            registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
            procedimiento.setInt(1,registro.getCodigoPresupuesto());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaSolicitud().getTime()));
            procedimiento.setDouble(3, registro.getCantidadPresupuesto());
            procedimiento.setInt(4, registro.getCodigoEmpresa());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }   
        public void generarReporte(){
            switch(tipoDeOperacion){
                case NINGUNO:
                if(tblPresupuesto.getSelectionModel().getSelectedItem() != null){
                imprimirReporte();
                }else{
                    JOptionPane.showMessageDialog(null,"Debe seleccionar una fila de datos para generar un reporte");
                            
                }
                break;
                    
        }
       }
        public void imprimirReporte(){
            Map parametros = new HashMap();
            int codigoEmpresa = Integer.valueOf(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
            parametros.put("codigoEmpresa",codigoEmpresa);
             GenerarReporte.mostarReporte("ReportePresupuesto.jasper","Reporte Presupuesto",parametros);
        }
        
        
        
        
        
        
        
        
        
      //CRUD CON FK 
      public void desactivarControles(){
        txtCodigoPresupuesto.setEditable(false);
        txtCantidadPresupuesto.setEditable(false);
        grpFechaSolicitud.setDisable(false);
        cmbCodigoEmpresa.setDisable(false);
    }
    
    public void activarControles(){
        txtCodigoPresupuesto.setEditable(false);
        txtCantidadPresupuesto.setEditable(true);
        grpFechaSolicitud.setDisable(false);
        cmbCodigoEmpresa.setEditable(false);
    }
    
    public void limpiarControles(){
        txtCodigoPresupuesto.setText("");
        txtCantidadPresupuesto.setText("");
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
    public void VentanaPresupuesto(){
        escenarioPrincipal.VentanaPresupuesto();
    }
    
    
    
}

   