package org.example.interfaccia_grafica;

import domain.Ruolo;
import domain.Utente;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private Button admin_close;


    @FXML
    private AnchorPane admin_form;

    @FXML
    private Button admin_login;

    @FXML
    private ImageView admin_minimize;

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

    @FXML
    private TextField admin_username;

    public void utente_close() {
        System.exit(0);
    }

    public void utente_minimize() {
        Stage stage = (Stage) utente_form.getScene().getWindow();
        stage.setIconified(true);
    }

    public void admin_close() {
        System.exit(0);
    }

    public void admin_minimize() {
        Stage stage = (Stage) admin_form.getScene().getWindow();
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


    private double x = 0;
    private double y = 0;

    @FXML
    private void handleAdminLogin() throws IOException {
        // Aggiungi qui il codice per verificare le credenziali dell'admin
        String username = admin_username.getText();
        String password = admin_password.getText();

        if (checkAdminCredentials(username, password)) {
            admin_form.setVisible(true);
            utente_form.setVisible(false);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Accesso Riuscito");
            alert.setHeaderText(null);
            alert.setContentText("Login effettuato con successo!");
            alert.showAndWait();

            admin_login.getScene().getWindow().hide();


            Parent root = FXMLLoader.load(getClass().getResource("admin-dashboard2.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);

            root.setOnMousePressed((MouseEvent event) -> {

                x = event.getSceneX();
                y = event.getSceneY();
            });

            root.setOnMouseDragged((MouseEvent event) -> {

                stage.setX(event.getScreenX() - x);
                stage.setY(event.getScreenY() - y);
            });

            stage.initStyle(StageStyle.TRANSPARENT);

            stage.setScene(scene);
            stage.show();

        } else {
            // Mostra un messaggio di errore se le credenziali sono sbagliate
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore di accesso");
            alert.setHeaderText(null);
            alert.setContentText("Credenziali admin non valide!");
            alert.showAndWait();
        }
    }

    private boolean checkAdminCredentials(String username, String password) {
        String credentialsFile = "admin_credentials.txt";

        // Verifica se il file delle credenziali esiste, altrimenti lo crea
        if (!Files.exists(Paths.get(credentialsFile))) {
            createAdminCredentialsFile(credentialsFile);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(credentialsFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                String storedUsername = parts[0];
                String storedPassword = parts[1];
                if (username.equals(storedUsername) && password.equals(storedPassword)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void createAdminCredentialsFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Credenziali di default impostate a nome: admin e password: admin
            writer.println("admin admin");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double x1 = 0;
    private double y1 = 0;

    @FXML
    private void handleLoginUtenteForm() throws IOException {

        // Raccoglie i dati dell'utente dai campi di testo
        String nome = utente_nome.getText();
        String cognome = utente_cognome.getText();
        int eta = Integer.parseInt(utente_eta.getText()); // Assicurati che utente_eta contenga un numero valido

        // Crea l'oggetto Utente
        Utente utente = new Utente(nome, cognome, eta, Ruolo.UTENTE); // Sostituisci Ruolo.UTENTE con il valore appropriato

        // Nasconde la finestra corrente
        utente_accedi.getScene().getWindow().hide();

        // Utilizza FXMLLoader per caricare il file FXML e ottenere il controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("user_dashboard.fxml"));
        Parent root = loader.load();

        // Ottenere il controller della dashboard dell'utente e impostare l'utente
        UserDashboardController dashboardController = loader.getController();
        dashboardController.setUtente(utente);

        // Prepara la nuova finestra (stage) con il root ottenuto dal FXML
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.initStyle(StageStyle.TRANSPARENT); // Imposta lo stile prima di visualizzare lo stage

        // Imposta i gestori per gli eventi del mouse per il trascinamento della finestra
        root.setOnMousePressed(event -> {
            x1 = event.getSceneX();
            y1 = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - x1);
            stage.setY(event.getScreenY() - y1);
        });

        stage.setScene(scene);
        stage.show();
    }

}