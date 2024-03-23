package org.example.interfaccia_grafica.film;

import Serializzazione.adapter.adaptee.FilmSerializer;
import Serializzazione.adapter.adapter.FilmSerializerAdapter;
import Serializzazione.adapter.target.IDataSerializer;
import cinema_Infrastructure.film.IFilm;
import domain.Amministratore;
import domain.Ruolo;
import exception.film.DurataFilmNonValidaException;
import exception.film.FilmGiaPresenteException;
import exception.film.TitoloVuotoException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.example.interfaccia_grafica.general_utility_classes.AlertUtil;
import prova_id_PERSISTENTE.GeneratoreIDPersistenteFilm;
import prova_id_PERSISTENTE.IGeneratoreIDPersistente;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GestioneFilmController implements Initializable {
    @FXML
    private TableColumn<IFilm, Long> IDFilmCol_tableview;

    @FXML
    private TextField IDRimuoviFIlm_textfield;

    @FXML
    private AnchorPane aggiungiFilm_anchorPane;

    @FXML
    private Button aggiungiFilm_btn;

    @FXML
    private ImageView aggiungiFilm_imageview;

    @FXML
    private TableColumn<IFilm, Integer> durataFilmCol_tableview;

    @FXML
    private TextField durataFilm_textfield;

    @FXML
    private Button eliminaFilm_btn;

    @FXML
    private TableView<IFilm> film_tableview;

    @FXML
    private TableColumn<IFilm, String> genereFilmCol_tableview;

    @FXML
    private TextField genereFilm_textfield;

    @FXML
    private Button importaFilmImageview_btn;

    @FXML
    private AnchorPane rimuoviFilm_anchorpane;

    @FXML
    private Button rimuoviFilm_btn;

    @FXML
    private TableColumn<IFilm, String> titoloFilmCol_tableview;

    @FXML
    private TextField titoloFilm_textfield;

    @FXML
    private Button tornaIndietroRimuoviFilm_btn;

    private ObservableList<IFilm> filmObservableList = FXCollections.observableArrayList();

    private List<IFilm> films;

    private IGestioneFilmService gestioneFilmService;


    @Override
    public void initialize(URL location, ResourceBundle resources){
        IDataSerializer filmSerializerAdapter = new FilmSerializerAdapter(new FilmSerializer());
        try {
            films = (List<IFilm>) filmSerializerAdapter.deserialize("film.ser");
            filmObservableList.addAll(films);
        } catch (Exception e) {
            System.out.println("Impossibile caricare i film esistenti. " + e.getMessage());
            films = new ArrayList<>();
        }

        IGeneratoreIDPersistente generatoreIDFilm = new GeneratoreIDPersistenteFilm();
        Amministratore amministratore = new Amministratore("Mario","Rossi",Ruolo.AMMINISTRATORE);
        this.gestioneFilmService = new GestioneFilmService(films, generatoreIDFilm, filmSerializerAdapter, amministratore);

        titoloFilmCol_tableview.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        durataFilmCol_tableview.setCellValueFactory(new PropertyValueFactory<>("durata"));
        genereFilmCol_tableview.setCellValueFactory(new PropertyValueFactory<>("genere"));
        IDFilmCol_tableview.setCellValueFactory(new PropertyValueFactory<>("id"));

        film_tableview.setItems(filmObservableList);

    }



    @FXML
    public void aggiungiFilm() {
        try {
            String titoloFilm = titoloFilm_textfield.getText();
            String genereFilm = genereFilm_textfield.getText();
            int durataFilm = Integer.parseInt(durataFilm_textfield.getText());

            gestioneFilmService.aggiungiFilm(titoloFilm, durataFilm, genereFilm, nuovoFilm -> {
                AlertUtil.showInformationAlert("Film aggiunto con successo!");
                aggiungiFilmTable(nuovoFilm);
            });
        } catch (FilmGiaPresenteException e) {
            AlertUtil.showErrorAlert("Il film è già presente: " + e.getMessage());
        } catch (DurataFilmNonValidaException e) {
            AlertUtil.showErrorAlert("Durata del film non valida: " + e.getMessage());
        } catch (TitoloVuotoException e) {
            AlertUtil.showErrorAlert("Il titolo del film non può essere vuoto.");
        } catch (NumberFormatException e) {
            AlertUtil.showErrorAlert("Formato numero non valido. Assicurati che la durata del film sia un numero.");
        } catch (Exception e) {
            AlertUtil.showErrorAlert("Errore imprevisto durante l'aggiunta del film: " + e.getMessage());
        }
    }



    @FXML
    public void rimuoviFilm() {
        try {
            long idFilm = Long.parseLong(IDRimuoviFIlm_textfield.getText());

            gestioneFilmService.rimuoviFilm(idFilm, idFilmRimosso -> {
                AlertUtil.showInformationAlert("Film rimosso con successo!");
                rimuoviFilmTable(idFilmRimosso);
            });
        } catch (NumberFormatException e) {
            AlertUtil.showErrorAlert("ID del film non valido.");
        } catch (Exception e) {
            AlertUtil.showErrorAlert("Errore durante la rimozione del film: " + e.getMessage());
        }
    }


    public void aggiungiFilmTable(IFilm nuovoFilm) {
        filmObservableList.add(nuovoFilm);
    }

    public void rimuoviFilmTable(long idSalaDaRimuovere) {
        // Trova la sala con l'ID specificato
        IFilm filmTrovato = null;
        for (IFilm film : filmObservableList) {
            if (film.getId() == idSalaDaRimuovere) {
                filmTrovato = film;
                break;
            }
        }

        // Rimuovi la sala trovata dalla lista osservabile, se presente
        if (filmTrovato != null) {
            filmObservableList.remove(filmTrovato);
        }
    }

    @FXML
    public void switchFilm(ActionEvent event){
        if(event.getSource() == rimuoviFilm_btn){
            aggiungiFilm_anchorPane.setVisible(false);
            rimuoviFilm_anchorpane.setVisible(true);

        }else if(event.getSource() == tornaIndietroRimuoviFilm_btn){
            aggiungiFilm_anchorPane.setVisible(true);
            rimuoviFilm_anchorpane.setVisible(false);
        }
    }
}

