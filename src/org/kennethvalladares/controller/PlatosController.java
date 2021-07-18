
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.kennethvalladares.bean.Plato;
import org.kennethvalladares.bean.TipoPlato;
import org.kennethvalladares.db.Conexion;
import org.kennethvalladares.system.Principal;

/**
 *
 * @author luis valladares
 */
public class PlatosController implements Initializable{
    private enum operaciones {NUEVO,GUARDAR,ELIMIAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
      private operaciones tipoDeOperacion = operaciones.NINGUNO;
     private Principal escenarioPrincipal;
     private ObservableList<Plato> listaPlato;
     private ObservableList<TipoPlato> listaTipoPlato;
     
     @FXML private TextField txtCodigoPlato;
     @FXML private TextField txtCantidad;
     @FXML private TextField txtNombrePlato;
     @FXML private TextField txtDescripcionPlato;
     @FXML private TextField  txtPrecioPlato;
     @FXML private ComboBox cmbCodigoPlato;
     @FXML private Button btnNuevo;
     @FXML private Button btnEliminar;
     @FXML private Button btnEditar;
     @FXML private Button btnReporte; 
     @FXML private TableView tblPlatos;
     @FXML private TableColumn colCodigoPlato;
     @FXML private TableColumn colCantidad;
     @FXML private TableColumn colNombrePlato;
     @FXML private TableColumn colDescripcionPlato;
     @FXML private TableColumn colPrecioPlato;
     @FXML private TableColumn colCodigoTipoPlato;
     
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    
        cargarDatos();
        cmbCodigoPlato.setItems(getTipoPlato());
    }
    
    public void cargarDatos(){
        tblPlatos.setItems(getPlato());
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("codigoPlato"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("cantidad"));
        colNombrePlato.setCellValueFactory(new PropertyValueFactory<Plato, String>("nombrePlato"));
        colDescripcionPlato.setCellValueFactory(new PropertyValueFactory<Plato, String>("descripcionPlato"));
        colPrecioPlato.setCellValueFactory(new PropertyValueFactory<Plato, Float>("precioPlato"));
        colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("codigoTipoPlato"));
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
    public ObservableList<TipoPlato>getTipoPlato(){
            ArrayList<TipoPlato> lista = new ArrayList<TipoPlato> ();
                try{
                    PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoPlato}");
                    ResultSet resultado = procedimiento.executeQuery();
                while(resultado.next()){
                    lista.add(new TipoPlato ( resultado.getInt("codigoTipoPlato"),
                                              resultado.getString("descripcionTipo")));
                }
                }catch(Exception e){
                    e.printStackTrace();
                }
                  return  listaTipoPlato = FXCollections.observableArrayList(lista);
    }
    public void seleccionarElemento(){
        txtCodigoPlato.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
        txtCantidad.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCantidad()));
        txtNombrePlato.setText(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getNombrePlato());
        txtDescripcionPlato.setText(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getDescripcionPlato());
       txtPrecioPlato.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getPrecioPlato()));
       cmbCodigoPlato.getSelectionModel().select(buscarTipoPlato(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
    }
   
      public TipoPlato buscarTipoPlato (int codigoTipoPlato){
        TipoPlato resultado = null;
            try{
               PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoPlato(?)}");
               procedimiento.setInt(1, codigoTipoPlato);
               ResultSet registro = procedimiento.executeQuery();
                while(registro.next()){
                    resultado = new TipoPlato ( registro.getInt("codigoTipoPlato"),
                            registro.getString("descripcionTipo"));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        return resultado;
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
        if (txtCantidad.getText().isEmpty() == true){
            JOptionPane.showMessageDialog(null, "Por favor ingrese un cantidad", "Agregar Plato", JOptionPane.INFORMATION_MESSAGE);
        }else if (txtNombrePlato.getText().isEmpty() == true){
            JOptionPane.showMessageDialog(null, "Por favot ingrese un nombre", "Agregar Plato", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtDescripcionPlato.getText().isEmpty() == true){
            JOptionPane.showMessageDialog(null, "por favor ingrese una direccion", "Agregar Plato", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtPrecioPlato.getText().isEmpty() == true){
            JOptionPane.showMessageDialog(null, "Por favor ingrese un precio", "Agregar Plato", JOptionPane.INFORMATION_MESSAGE);
        }else if(cmbCodigoPlato.getSelectionModel().getSelectedItem() == null){
            JOptionPane.showMessageDialog(null, "Por favor escoja un codigo de Tipo Plato", "Agregar Plato", JOptionPane.INFORMATION_MESSAGE);
        }else{
        Plato registro = new Plato();
        registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
        registro.setNombrePlato(txtNombrePlato.getText());
        registro.setDescripcionPlato(txtDescripcionPlato.getText());
        registro.setPrecioPlato(Float.parseFloat(txtPrecioPlato.getText()));
        registro.setCodigoTipoPlato(((TipoPlato)this.cmbCodigoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarPlato(?,?,?,?,?)}");
                procedimiento.setInt(1,registro.getCantidad());
                procedimiento.setString(2, registro.getNombrePlato());
                procedimiento.setString(3 ,registro.getDescripcionPlato());
                procedimiento.setFloat(4, registro.getPrecioPlato());
                procedimiento.setInt(5, registro.getCodigoTipoPlato());
                procedimiento.execute();
                listaPlato.add(registro);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
     public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblPlatos.getSelectionModel().getSelectedItem() != null){
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
    public void actualizar(){
        try{
         PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarPlato(?,?,?,?,?,?)}");
         Plato registro = (Plato)tblPlatos.getSelectionModel().getSelectedItem();
         registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
         registro.setNombrePlato(txtNombrePlato.getText());
         registro.setDescripcionPlato(txtDescripcionPlato.getText());
         registro.setPrecioPlato(Float.parseFloat(txtPrecioPlato.getText()));
         
         procedimiento.setInt(1, registro.getCodigoPlato());
         procedimiento.setInt(2, registro.getCantidad());
         procedimiento.setString(3,registro.getNombrePlato());
         procedimiento.setString(4,registro.getDescripcionPlato());
         procedimiento.setFloat(5, registro.getPrecioPlato());
         procedimiento.setInt(6, registro.getCodigoTipoPlato());
         procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
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
                if(tblPlatos.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro de eliminar un dato?","Eliminar Plato",JOptionPane.YES_NO_OPTION);
                    if(respuesta == JOptionPane.YES_OPTION)
                            try{
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ElimiarPlato(?)}");
                                procedimiento.setInt(1, ((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato());
                                procedimiento.execute();
                                listaPlato.remove(tblPlatos.getSelectionModel().getSelectedIndex());
                                limpiarControles();
                            }catch(Exception e){
                                e.printStackTrace();
                            }else{
                        JOptionPane.showMessageDialog(null,"Debe seleccionar un elemento");
                    }
                }
        }
    }
    
    public void desactivarControles(){
        txtCodigoPlato.setEditable(false);
        txtCantidad.setEditable(false);
        txtNombrePlato.setEditable(false);
        txtDescripcionPlato.setEditable(false);
        txtPrecioPlato.setEditable(false);
    }
    public void activarControles(){
        txtCodigoPlato.setEditable(false);
        txtCantidad.setEditable(true);
        txtNombrePlato.setEditable(true);
        txtDescripcionPlato.setEditable(true);
        txtPrecioPlato.setEditable(true);
    }
    public void limpiarControles(){
       txtCodigoPlato.setText("");
       txtCantidad.setText("");
       txtNombrePlato.setText("");
       txtDescripcionPlato.setText("");
       txtPrecioPlato.setText("");
       cmbCodigoPlato.getSelectionModel().clearSelection();
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
    public void VentanaTipoPlato(){
        escenarioPrincipal.VentanaTipoPlato();
    }
    
}
