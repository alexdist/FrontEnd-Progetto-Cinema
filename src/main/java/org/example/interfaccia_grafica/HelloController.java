package org.example.interfaccia_grafica;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private Button admin_close;

    @FXML
    private TextField admin_cognome;

    @FXML
    private Button admin_login;

    @FXML
    private ImageView admin_minimize;

    @FXML
    private TextField admin_nome;

    @FXML
    private PasswordField admin_password;

    @FXML
    private Button utente_accedi;

    @FXML
    private ImageView utente_close;

    @FXML
    private TextField utente_cognome;

    @FXML
    private TextField utente_eta;

    @FXML
    private ImageView utente_minimize;

    @FXML
    private TextField utente_nome;

    @FXML
    private AnchorPane utente_form;

    public void utente_close(){
        System.exit(0);
    }

    public void utente_minimize(){
        Stage stage = (Stage)utente_form.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}