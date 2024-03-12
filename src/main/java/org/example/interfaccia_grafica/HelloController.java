package org.example.interfaccia_grafica;

import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    private AnchorPane admin_form;

    @FXML
    private Button admin_login;

    @FXML
    private ImageView admin_minimize;

    @FXML
    private TextField admin_nome;

    @FXML
    private PasswordField admin_password;

    @FXML
    private Hyperlink hyperlink_admin;

    @FXML
    private Hyperlink hyperlink_utente;

    @FXML
    private Button login_accedi_admin;

    @FXML
    private Button login_accedi_utente;

    @FXML
    private AnchorPane login_form;

    @FXML
    private Button utente_accedi;

    @FXML
    private ImageView utente_close;

    @FXML
    private ImageView utente_close1;

    @FXML
    private TextField utente_cognome;

    @FXML
    private TextField utente_eta;

    @FXML
    private AnchorPane utente_form;

    @FXML
    private ImageView utente_minimize;

    @FXML
    private ImageView utente_minimize1;

    @FXML
    private TextField utente_nome;

    public void utente_close(){
        System.exit(0);
    }

    public void utente_minimize(){
        Stage stage = (Stage)utente_form.getScene().getWindow();
        stage.setIconified(true);
    }

    public void admin_close(){
        System.exit(0);
    }

    public void admin_minimize(){
        Stage stage = (Stage)admin_form.getScene().getWindow();
        stage.setIconified(true);
    }

    // Metodo per mostrare il pannello dell'utente

    @FXML
    private void handleLoginUtente() {
        utente_form.setVisible(true);
        admin_form.setVisible(false);
    }

    // Metodo per mostrare il pannello dell'admin
    @FXML
    private void handleLoginAdmin() {
        admin_form.setVisible(true);
        utente_form.setVisible(false);
    }

    @FXML
    private void handleBackToMainFromAdmin() {
        admin_form.setVisible(false); // Nasconde il form admin
        login_form.setVisible(true); // Mostra il form principale
    }

    @FXML
    private void handleBackToMainFromUtente() {
        utente_form.setVisible(false); // Nasconde il form utente
        login_form.setVisible(true); // Mostra il form principale
    }



    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}