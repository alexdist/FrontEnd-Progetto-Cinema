package org.example.interfaccia_grafica;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {


    @FXML
    private TableColumn<?, ?> IDCol_tableview;

    @FXML
    private TableColumn<?, ?> IDFilmCol_tableview;

    @FXML
    private TextField IDRimuoviFIlm_textfield;

    @FXML
    private TextField IDRimuoviSala_textfiel;

    @FXML
    private TextField IDRimuoviSpett_textfield;

    @FXML
    private TableColumn<?, ?> IDSpettacoloCol_tableview;

    @FXML
    private TextField IDmodFilmSpett_textfield;

    @FXML
    private TextField IDmodOrarioSpett_textfield;

    @FXML
    private TextField IDmodSalaSpett_textfield;

    @FXML
    private TableColumn<?, ?> affluenzaSalaCol_tableview;

    @FXML
    private AnchorPane aggElimSpett_anchorpane;

    @FXML
    private TextField aggFilmSpett_textfield;

    @FXML
    private TextField aggOrarioSpett_textfield;

    @FXML
    private TextField aggSalaSpett_textfield;

    @FXML
    private AnchorPane aggiungiFilm_anchorPane;

    @FXML
    private Button aggiungiFilm_btn;

    @FXML
    private ImageView aggiungiFilm_imageview;

    @FXML
    private Button aggiungiSala_btn;

    @FXML
    private Button aggiungiSpettacolo_btn;

    @FXML
    private TableColumn<?, ?> capacitaCol_tableview;

    @FXML
    private TextField capacitaSala_textflied;

    @FXML
    private Button close;

    @FXML
    private Button dashboard_btn;

    @FXML
    private Label dashboard_filmInProgTotali;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private Label dashboard_saleTotali;

    @FXML
    private Label dashboard_spettInPalTotali;

    @FXML
    private TableColumn<?, ?> durataFilmCol_tableview;

    @FXML
    private TextField durataFilm_textfield;

    @FXML
    private Button eliminaFilm_btn;

    @FXML
    private Button eliminaSala_btn;

    @FXML
    private Button eliminaSpettacolo_btn;

    @FXML
    private TableColumn<?, ?> filmCol_tableview;

    @FXML
    private TableView<?> film_tableview;

    @FXML
    private Button generaRepRicavi_btn;

    @FXML
    private TableColumn<?, ?> genereFilmCol_tableview;

    @FXML
    private TextField genereFilm_textfield;

    @FXML
    private Button gestioneFilm_btn;

    @FXML
    private AnchorPane gestioneFilm_form;

    @FXML
    private Button gestionePrezzi_btn;

    @FXML
    private AnchorPane gestionePrezzi_form;

    @FXML
    private Button gestioneRicavi_btn;

    @FXML
    private AnchorPane gestioneRicavi_form;

    @FXML
    private Button gestioneSale_btn;

    @FXML
    private AnchorPane gestioneSale_form;

    @FXML
    private Button gestioneSpettacoli_btn;

    @FXML
    private AnchorPane gestioneSpettacoli_form;

    @FXML
    private Button importaFilmImageview_btn;

    @FXML
    private Button impostaPrzIntero_btn;

    @FXML
    private TextField impostaPrzIntero_textflied;

    @FXML
    private Button impostaPrzRidotto_btn;

    @FXML
    private TextField impostaPrzRidotto_textflied;

    @FXML
    private Button minimize;

    @FXML
    private TextField modFilmSpett_textfield;

    @FXML
    private TextField modOrarioSpett_textfield;

    @FXML
    private TextField modSalaSpett_textfield;

    @FXML
    private AnchorPane modSpettacolo_anchorpane;

    @FXML
    private Button modificaFilmSpett_btn;

    @FXML
    private Button modificaOrarioSpett_btn;

    @FXML
    private Button modificaSalaSpett_btn;

    @FXML
    private TableColumn<?, ?> numeroSalaCol_tableview;

    @FXML
    private TextField numeroSala_textfiel;

    @FXML
    private TableColumn<?, ?> orarioProiezioneCol_tableview;

    @FXML
    private TableView<?> prezzi_tableview;

    @FXML
    private TableColumn<?, ?> prezzoInteroCol_tableview;

    @FXML
    private TableColumn<?, ?> prezzoRidottoCol_tableview;

    @FXML
    private TableColumn<?, ?> ricaviRicavoCol_tableview;

    @FXML
    private TableColumn<?, ?> ricaviSalaCol_tableview;

    @FXML
    private TableView<?> ricavi_tableview;

    @FXML
    private AnchorPane rimuoviFilm_anchorpane;

    @FXML
    private Button rimuoviFilm_btn;

    @FXML
    private TableColumn<?, ?> salaCol_tableview;

    @FXML
    private TableView<?> sala_tableview;

    @FXML
    private TableColumn<?, ?> spettacoloCol_tableview;

    @FXML
    private TableView<?> spettacolo_tableview;

    @FXML
    private Button switchModificaSpett_btn;

    @FXML
    private Button switchTornaIndietroSpett_btn;

    @FXML
    private TableColumn<?, ?> titoloFilmCol_tableview;

    @FXML
    private TextField titoloFilm_textfiel;

    @FXML
    private AnchorPane topForm_anchorpane;

    @FXML
    private Button tornaIndietroRimuoviFilm_btn;

    @FXML
    private Button esci_btn;

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

    public void switchForm(ActionEvent event){

        if(event.getSource() == dashboard_btn){
            dashboard_form.setVisible(true);
            gestioneSale_form.setVisible(false);
            gestioneFilm_form.setVisible(false);
            gestioneSpettacoli_form.setVisible(false);
            gestionePrezzi_form.setVisible(false);
            gestioneRicavi_form.setVisible(false);

            dashboard_btn.setStyle("-fx-background-color: #ae2d3c");
            gestioneSale_btn.setStyle("-fx-background-color: transparent");
            gestioneFilm_btn.setStyle("-fx-background-color: transparent");
            gestioneSpettacoli_btn.setStyle("-fx-background-color: transparent");
            gestionePrezzi_btn.setStyle("-fx-background-color: transparent");
            gestioneRicavi_btn.setStyle("-fx-background-color: transparent");


        }else if(event.getSource() == gestioneSale_btn){
            dashboard_form.setVisible(false);
            gestioneSale_form.setVisible(true);
            gestioneFilm_form.setVisible(false);
            gestioneSpettacoli_form.setVisible(false);
            gestionePrezzi_form.setVisible(false);
            gestioneRicavi_form.setVisible(false);

            dashboard_btn.setStyle("-fx-background-color: transparent");
            gestioneSale_btn.setStyle("-fx-background-color: #ae2d3c");
            gestioneFilm_btn.setStyle("-fx-background-color: transparent");
            gestioneSpettacoli_btn.setStyle("-fx-background-color: transparent");
            gestionePrezzi_btn.setStyle("-fx-background-color: transparent");
            gestioneRicavi_btn.setStyle("-fx-background-color: transparent");

        }else if(event.getSource() == gestioneFilm_btn){
            dashboard_form.setVisible(false);
            gestioneSale_form.setVisible(false);
            gestioneFilm_form.setVisible(true);
            gestioneSpettacoli_form.setVisible(false);
            gestionePrezzi_form.setVisible(false);
            gestioneRicavi_form.setVisible(false);

            dashboard_btn.setStyle("-fx-background-color: transparent");
            gestioneSale_btn.setStyle("-fx-background-color: transparent");
            gestioneFilm_btn.setStyle("-fx-background-color: #ae2d3c");
            gestioneSpettacoli_btn.setStyle("-fx-background-color: transparent");
            gestionePrezzi_btn.setStyle("-fx-background-color: transparent");
            gestioneRicavi_btn.setStyle("-fx-background-color: transparent");

        }else if(event.getSource() == gestioneSpettacoli_btn){
            dashboard_form.setVisible(false);
            gestioneSale_form.setVisible(false);
            gestioneFilm_form.setVisible(false);
            gestioneSpettacoli_form.setVisible(true);
            gestionePrezzi_form.setVisible(false);
            gestioneRicavi_form.setVisible(false);

            dashboard_btn.setStyle("-fx-background-color: transparent");
            gestioneSale_btn.setStyle("-fx-background-color: transparent");
            gestioneFilm_btn.setStyle("-fx-background-color: transparent");
            gestioneSpettacoli_btn.setStyle("-fx-background-color: #ae2d3c");
            gestionePrezzi_btn.setStyle("-fx-background-color: transparent");
            gestioneRicavi_btn.setStyle("-fx-background-color: transparent");


        }else if(event.getSource() == gestionePrezzi_btn){
            dashboard_form.setVisible(false);
            gestioneSale_form.setVisible(false);
            gestioneFilm_form.setVisible(false);
            gestioneSpettacoli_form.setVisible(false);
            gestionePrezzi_form.setVisible(true);
            gestioneRicavi_form.setVisible(false);

            dashboard_btn.setStyle("-fx-background-color: transparent");
            gestioneSale_btn.setStyle("-fx-background-color: transparent");
            gestioneFilm_btn.setStyle("-fx-background-color: transparent");
            gestioneSpettacoli_btn.setStyle("-fx-background-color: transparent");
            gestionePrezzi_btn.setStyle("-fx-background-color: #ae2d3c");
            gestioneRicavi_btn.setStyle("-fx-background-color: transparent");

        }else if(event.getSource() == gestioneRicavi_btn){
            dashboard_form.setVisible(false);
            gestioneSale_form.setVisible(false);
            gestioneFilm_form.setVisible(false);
            gestioneSpettacoli_form.setVisible(false);
            gestionePrezzi_form.setVisible(false);
            gestioneRicavi_form.setVisible(true);

            dashboard_btn.setStyle("-fx-background-color: transparent");
            gestioneSale_btn.setStyle("-fx-background-color: transparent");
            gestioneFilm_btn.setStyle("-fx-background-color: transparent");
            gestioneSpettacoli_btn.setStyle("-fx-background-color: transparent");
            gestionePrezzi_btn.setStyle("-fx-background-color: transparent");
            gestioneRicavi_btn.setStyle("-fx-background-color: #ae2d3c");
        }
    }

    public void switchFilm(ActionEvent event){
        if(event.getSource() == rimuoviFilm_btn){
            aggiungiFilm_anchorPane.setVisible(false);
            rimuoviFilm_anchorpane.setVisible(true);

        }else if(event.getSource() == tornaIndietroRimuoviFilm_btn){
            aggiungiFilm_anchorPane.setVisible(true);
            rimuoviFilm_anchorpane.setVisible(false);
        }
    }

    public void switchSpettacolo(ActionEvent event){
        if(event.getSource() == switchModificaSpett_btn){
            aggElimSpett_anchorpane.setVisible(false);
            modSpettacolo_anchorpane.setVisible(true);

        }else if(event.getSource() == switchTornaIndietroSpett_btn){
            aggElimSpett_anchorpane.setVisible(true);
            modSpettacolo_anchorpane.setVisible(false);
        }
    }

    public void closeDashboard(){

        System.exit(0);
    }

    public void minimizeDashboard(){

        Stage stage = (Stage)topForm_anchorpane.getScene().getWindow();
        stage.setIconified(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
