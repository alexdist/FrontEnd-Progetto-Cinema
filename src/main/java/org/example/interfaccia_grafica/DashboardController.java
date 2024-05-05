package org.example.interfaccia_grafica;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private Button close;

    @FXML
    private Button dashboard_btn;

    @FXML
    private Button esci_btn;

    @FXML
    private Button gestioneFilm_btn;

    @FXML
    private Button gestionePrezzi_btn;

    @FXML
    private Button gestioneRicavi_btn;

    @FXML
    private Button gestioneSale_btn;

    @FXML
    private Button gestioneSpettacoli_btn;

    @FXML
    private Button minimize;

    @FXML
    private AnchorPane topForm_anchorpane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadInitialDashboardView();
    }

    private void loadInitialDashboardView() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-dashboard-hello.fxml"));
            Node initialView = loader.load();
            mainBorderPane.setCenter(initialView);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @FXML
    private double x = 0;
    private double y = 0;

    public void logout() throws IOException {
        esci_btn.getScene().getWindow().hide();
        try{
            Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));

            Stage stage = new Stage();
            Scene scene = new Scene(root);

            root.setOnMousePressed((MouseEvent event)->{

                x = event.getSceneX();
                y = event.getSceneY();

            });

            root.setOnMouseDragged((MouseEvent event)->{

                stage.setX(event.getScreenX() - x);
                stage.setY(event.getScreenY() - y);

            });

            stage.setScene(scene);
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    private BorderPane mainBorderPane;

    public void switchForm(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            String fxmlFile = ""; // Il percorso del file FXML da caricare

            if(event.getSource() == dashboard_btn) {
                fxmlFile = "admin-dashboard-hello.fxml";
                dashboard_btn.setStyle("-fx-background-color: #ae2d3c");
                gestioneSale_btn.setStyle("-fx-background-color: transparent");
                gestioneFilm_btn.setStyle("-fx-background-color: transparent");
                gestioneSpettacoli_btn.setStyle("-fx-background-color: transparent");
                gestionePrezzi_btn.setStyle("-fx-background-color: transparent");
                gestioneRicavi_btn.setStyle("-fx-background-color: transparent");
            }
            else if (event.getSource() == gestioneSale_btn) {
                fxmlFile = "gestione-sale.fxml";
                dashboard_btn.setStyle("-fx-background-color: transparent");
                gestioneSale_btn.setStyle("-fx-background-color: #ae2d3c");
                gestioneFilm_btn.setStyle("-fx-background-color: transparent");
                gestioneSpettacoli_btn.setStyle("-fx-background-color: transparent");
                gestionePrezzi_btn.setStyle("-fx-background-color: transparent");
                gestioneRicavi_btn.setStyle("-fx-background-color: transparent");

            } else if (event.getSource() == gestioneFilm_btn) {
                fxmlFile = "gestione-film.fxml";
                dashboard_btn.setStyle("-fx-background-color: transparent");
                gestioneSale_btn.setStyle("-fx-background-color: transparent");
                gestioneFilm_btn.setStyle("-fx-background-color: #ae2d3c");
                gestioneSpettacoli_btn.setStyle("-fx-background-color: transparent");
                gestionePrezzi_btn.setStyle("-fx-background-color: transparent");
                gestioneRicavi_btn.setStyle("-fx-background-color: transparent");

            } else if (event.getSource() == gestioneSpettacoli_btn) {
                fxmlFile = "gestione-spettacoli.fxml";
                dashboard_btn.setStyle("-fx-background-color: transparent");
                gestioneSale_btn.setStyle("-fx-background-color: transparent");
                gestioneFilm_btn.setStyle("-fx-background-color: transparent");
                gestioneSpettacoli_btn.setStyle("-fx-background-color: #ae2d3c");
                gestionePrezzi_btn.setStyle("-fx-background-color: transparent");
                gestioneRicavi_btn.setStyle("-fx-background-color: transparent");


            } else if (event.getSource() == gestionePrezzi_btn) {
                fxmlFile = "gestione-prezzi.fxml";
                dashboard_btn.setStyle("-fx-background-color: transparent");
                gestioneSale_btn.setStyle("-fx-background-color: transparent");
                gestioneFilm_btn.setStyle("-fx-background-color: transparent");
                gestioneSpettacoli_btn.setStyle("-fx-background-color: transparent");
                gestionePrezzi_btn.setStyle("-fx-background-color: #ae2d3c");
                gestioneRicavi_btn.setStyle("-fx-background-color: transparent");

            } else if (event.getSource() == gestioneRicavi_btn) {
                fxmlFile = "gestione-ricavi.fxml";
                dashboard_btn.setStyle("-fx-background-color: transparent");
                gestioneSale_btn.setStyle("-fx-background-color: transparent");
                gestioneFilm_btn.setStyle("-fx-background-color: transparent");
                gestioneSpettacoli_btn.setStyle("-fx-background-color: transparent");
                gestionePrezzi_btn.setStyle("-fx-background-color: transparent");
                gestioneRicavi_btn.setStyle("-fx-background-color: #ae2d3c");
            }

            loader.setLocation(getClass().getResource(fxmlFile));
            Node node = loader.load();



            mainBorderPane.setCenter(node);

        } catch (Exception e) { // Cattura una più ampia varietà di eccezioni
            e.printStackTrace();
            showErrorDialog();
        }
    }

    private void showErrorDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore di Caricamento");
        alert.setHeaderText("Impossibile Caricare la Vista");
        alert.setContentText("Si è verificato un errore durante il caricamento della vista desiderata. Si prega di riprovare.");
        alert.showAndWait();
    }


    public void closeDashboard(){

        System.exit(0);
    }

    public void minimizeDashboard(){

        Stage stage = (Stage)topForm_anchorpane.getScene().getWindow();
        stage.setIconified(true);
    }

}

