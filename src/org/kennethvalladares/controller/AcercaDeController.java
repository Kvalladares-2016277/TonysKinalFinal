package org.kennethvalladares.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.kennethvalladares.system.Principal;


public class AcercaDeController implements Initializable{
    
    private Principal principal;
    @FXML private Label lblTitulo;
    @FXML private Label lblNombre;
    @FXML private Label lblNombre1;
    @FXML private ImageView imgFoto;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public Principal getPrincipal() {
        return principal;
    }

    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }
    
    public void programador(){
        Image image = new Image(getClass().getResourceAsStream("/org/kennethvalladares/image/perfil.jpeg"));
        this.lblTitulo.setText("PROGRAMADOR");
        this.lblNombre.setText("Kenneth Luis Ernesto Valladares Salguero");
        this.lblNombre1.setText("2016277");
        this.imgFoto.setImage(image);
    }
    
    public void administracion(){
        Image image = new Image(getClass().getResourceAsStream("/org/kennethvalladares/image/Kinal.png"));
        this.lblTitulo.setText("ADMINISTRACION");
        this.lblNombre.setText("Centro Educativo Tecnico Laboral Kinal");
        this.lblNombre1.setText("Quinto Perito Informatica A");
        this.imgFoto.setImage(image);
    }
    
    public void mostrarEscenarioPrincipal(){
        this.principal.menuPrincipal();
    }
    
}
