package org.example.interfaccia_grafica;

import Serializzazione.adapter.adaptee.PrezziBigliettoSerializer;
import Serializzazione.adapter.adapter.PrezziBigliettoSerializerAdapter;
import domain.Amministratore;
import domain.Ruolo;
import exception.film.*;
import exception.sala.SalaGiaEsistenteException;
import exception.sala.SalaNonTrovataException;
import exception.sala.SalaNonValidaException;
import exception.spettacolo.SovrapposizioneSpettacoloException;
import exception.spettacolo.SpettacoloNonTrovatoException;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.interfaccia_grafica.general_utility_classes.serializzazione.IPrezziDataSerializer;
import org.example.interfaccia_grafica.general_utility_classes.serializzazione.PrezziDataSerializer;
import org.example.interfaccia_grafica.service_gestioneprezzicontroller.IPrezziService;
import org.example.interfaccia_grafica.service_gestioneprezzicontroller.PrezziService;
import ticket_pricing.IPrezziBiglietto;
import ticket_pricing.PrezziBiglietto;
import java.io.IOException;
import java.util.Optional;
import static org.example.interfaccia_grafica.general_utility_classes.AlertUtil.showAlert;


public class GestionePrezziController {
    @FXML
    private Button impostaPrzIntero_btn;

    @FXML
    private TextField impostaPrzIntero_textflied;

    @FXML
    private Button impostaPrzRidotto_btn;

    @FXML
    private TextField impostaPrzRidotto_textflied;

    @FXML
    private TableView<IPrezziBiglietto> prezzi_tableview;

    @FXML
    private TableColumn<IPrezziBiglietto, Double> prezzoInteroCol_tableview;

    @FXML
    private TableColumn<IPrezziBiglietto, Double> prezzoRidottoCol_tableview;

    @FXML
    private TableColumn<IPrezziBiglietto, Integer> sovrapprezzoCol_tableview1;

    @FXML
    private Button impostaSovraPrz_btn1;

    @FXML
    private TextField impostaSovraPrz_textflied1;

    @FXML
    private Spinner<Double> sovrapprezzo_spinner;

    @FXML
    private IPrezziService prezziService;
    @FXML
    private IPrezziDataSerializer prezziDataSerializer;

   private Amministratore amministratore;

    @FXML
    private IPrezziBiglietto prezziBiglietto;

    @FXML
    public void initialize() {
        amministratore = new Amministratore("Mario", "Rossi", Ruolo.AMMINISTRATORE);

        prezziDataSerializer = new PrezziDataSerializer(new PrezziBigliettoSerializerAdapter(new PrezziBigliettoSerializer()));

        // caricamento dei prezzi salvati
        Optional<IPrezziBiglietto> prezziCaricati = prezziDataSerializer.caricaPrezzi();
        if (prezziCaricati.isPresent()) {
            prezziBiglietto = prezziCaricati.get();
            // Inizializza prezziService con i prezzi caricati
            prezziService = new PrezziService(prezziBiglietto, amministratore);
        } else {
            // Se non ci sono prezzi salvati, inizializza con valori di default
            prezziBiglietto = new PrezziBiglietto(0, 0);
            prezziService = new PrezziService(prezziBiglietto, amministratore);
        }

        // Configura lo Spinner per valori da 0.0 a 1.0, con incremento di 0.1
        //SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1.0, 0.0, 0.1);
        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 100.0, 0.0, 10.0);
        sovrapprezzo_spinner.setValueFactory(valueFactory);
        sovrapprezzo_spinner.setEditable(true); // Opzionale: rende lo Spinner editabile per l'input manuale

        impostaPrzIntero_btn.setOnAction(event -> impostaPrezzoIntero());
        impostaPrzRidotto_btn.setOnAction(event -> impostaPrezzoRidotto());
        impostaSovraPrz_btn1.setOnAction(event -> impostaSovrapprezzo());


        if (prezziBiglietto != null) {
            // Crea una ObservableList con un solo elemento: l'oggetto prezziBiglietto
            ObservableList<IPrezziBiglietto> data = FXCollections.observableArrayList(prezziBiglietto);

            // Imposta ObservableList come l'elemento della TableView
            prezzi_tableview.setItems(data);

            // Configura le TableColumn per mostrare i dati
            prezzoInteroCol_tableview.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPrezzoIntero()));
            prezzoRidottoCol_tableview.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPrezzoRidotto()));
            sovrapprezzoCol_tableview1.setCellValueFactory(cellData ->
                    //new SimpleObjectProperty<>((int) (cellData.getValue().getSovrapprezzo() * 100)));
                    new SimpleObjectProperty<>((int) (cellData.getValue().getSovrapprezzo())));

        }
    }

    private void aggiornaPrezziTableView() {
        if (prezziBiglietto != null) {
            // Semplicemente aggiorna la ObservableList con il nuovo oggetto prezziBiglietto
            // In questo caso specifico, dato che c'è un solo elemento, è possibile rimuoverlo e aggiungerlo di nuovo per forzare l'aggiornamento
            prezzi_tableview.getItems().clear();
            prezzi_tableview.getItems().add(prezziBiglietto);
        }
    }


private void impostaPrezzoIntero() {
    try {
        double nuovoPrezzoIntero = Double.parseDouble(impostaPrzIntero_textflied.getText());
        prezziService.impostaPrezzoIntero(nuovoPrezzoIntero);
        aggiornaPrezziTableView();

        showAlert("Prezzo Aggiornato", "Il prezzo intero è stato aggiornato con successo.");

    } catch (NumberFormatException | FilmGiaPresenteException | DurataFilmNonValidaException |
             TitoloVuotoException | FilmNonTrovatoException | SalaGiaEsistenteException | SalaNonTrovataException |
             SovrapposizioneSpettacoloException | FilmNonValidoException | SalaNonValidaException |
             SpettacoloNonTrovatoException e) {
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}

    private void impostaPrezzoRidotto() {
        try {
            double nuovoPrezzoRidotto = Double.parseDouble(impostaPrzRidotto_textflied.getText());
            prezziService.impostaPrezzoRidotto(nuovoPrezzoRidotto);
            aggiornaPrezziTableView();

            showAlert("Prezzo Aggiornato", "Il prezzo ridotto è stato aggiornato con successo.");

        } catch (NumberFormatException | FilmGiaPresenteException | DurataFilmNonValidaException |
                 TitoloVuotoException | FilmNonTrovatoException | SalaGiaEsistenteException | SalaNonTrovataException |
                 SovrapposizioneSpettacoloException | FilmNonValidoException | SalaNonValidaException |
                 SpettacoloNonTrovatoException | IOException e) {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void impostaSovrapprezzo() {
        try {
            double sovrapprezzo = sovrapprezzo_spinner.getValue();
            prezziService.impostaSovrapprezzo(sovrapprezzo);
            aggiornaPrezziTableView();

            showAlert("Prezzo Aggiornato", "Il sovrapprezzo è stato aggiornato con successo.");

        } catch (NumberFormatException | FilmGiaPresenteException | DurataFilmNonValidaException |
                 TitoloVuotoException | FilmNonTrovatoException | SalaGiaEsistenteException | SalaNonTrovataException |
                 SovrapposizioneSpettacoloException | FilmNonValidoException | SalaNonValidaException |
                 SpettacoloNonTrovatoException | IOException e) {
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}









