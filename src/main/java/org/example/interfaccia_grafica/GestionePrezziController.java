package org.example.interfaccia_grafica;

import Serializzazione.adapter.adapter.PrezziBigliettoSerializerAdapter;
import Serializzazione.adapter.target.IDataSerializer;
import admin_commands.prezzi_biglietto.ImpostaSovrapprezzoCommand;
import admin_commands.prezzi_biglietto.intero.ImpostaPrezzoInteroCommand;
import admin_commands.prezzi_biglietto.ridotto.ImpostaPrezzoRidottoCommand;
import admin_interfaces.ICommand;
import domain.Amministratore;
import domain.Ruolo;
import exception.film.*;
import exception.sala.SalaGiaEsistenteException;
import exception.sala.SalaNonTrovataException;
import exception.sala.SalaNonValidaException;
import exception.spettacolo.SovrapposizioneSpettacoloException;
import exception.spettacolo.SpettacoloNonTrovatoException;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ticket_pricing.IPrezziBiglietto;
import ticket_pricing.PrezziBiglietto;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

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

    // Istanza dell'oggetto prezziBiglietto (potrebbe essere iniettata o recuperata in altro modo)
   // private IPrezziBiglietto prezziBiglietto;

    // Istanza dell'amministratore (potrebbe essere iniettata o recuperata in altro modo)
    // private Amministratore amministratore;

    // Serializzatore per PrezziBiglietto
    private IDataSerializer prezziBigliettoSerializer = new PrezziBigliettoSerializerAdapter();

    Amministratore amministratore = new Amministratore("Mario", "Rossi", Ruolo.AMMINISTRATORE);

    IPrezziBiglietto prezziBiglietto = new PrezziBiglietto(0,0);

    @FXML
    public void initialize() {

        // Configura lo Spinner per valori da 0.0 a 1.0, con incremento di 0.1
        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1.0, 0.0, 0.1);
        sovrapprezzo_spinner.setValueFactory(valueFactory);
        sovrapprezzo_spinner.setEditable(true); // Opzionale: rende lo Spinner editabile per l'input manuale

        impostaPrzIntero_btn.setOnAction(event -> impostaPrezzoIntero());
        impostaPrzRidotto_btn.setOnAction(event -> impostaPrezzoRidotto());
        impostaSovraPrz_btn1.setOnAction(event -> impostaSovrapprezzo());
        // Carica i prezzi correnti all'avvio dell'applicazione, se esistono
        caricaPrezziDaFile();

        // Assumi che prezziBiglietto sia un campo della classe controller che è stato caricato o inizializzato nel metodo caricaPrezziDaFile()
        if (prezziBiglietto != null) {
            // Crea una ObservableList con un solo elemento: l'oggetto prezziBiglietto
            ObservableList<IPrezziBiglietto> data = FXCollections.observableArrayList(prezziBiglietto);

            // Imposta questa ObservableList come l'elemento della TableView
            prezzi_tableview.setItems(data);

            // Configura le TableColumn per mostrare i dati
            prezzoInteroCol_tableview.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPrezzoIntero()));
            prezzoRidottoCol_tableview.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPrezzoRidotto()));
            //sovrapprezzoCol_tableview1.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSovrapprezzo()*100));
            sovrapprezzoCol_tableview1.setCellValueFactory(cellData ->
                    new SimpleObjectProperty<>((int) (cellData.getValue().getSovrapprezzo() * 100)));

        }

       // caricaPrezziInTableView();
    }

    private void aggiornaPrezziTableView() {
        if (prezziBiglietto != null) {
            // Semplicemente aggiorna la ObservableList con il nuovo oggetto prezziBiglietto
            // In questo caso specifico, dato che c'è un solo elemento, puoi rimuoverlo e aggiungerlo di nuovo per forzare l'aggiornamento
            prezzi_tableview.getItems().clear();
            prezzi_tableview.getItems().add(prezziBiglietto);
        }
    }





    private void impostaPrezzoIntero() {
        try {
            double nuovoPrezzoIntero = Double.parseDouble(impostaPrzIntero_textflied.getText());
            ICommand cambiaPrezzoIntero = new ImpostaPrezzoInteroCommand(prezziBiglietto, nuovoPrezzoIntero);
            amministratore.setCommand(cambiaPrezzoIntero);
            amministratore.eseguiComando();
            aggiornaPrezziTableView();
            // Mostra un alert di successo
            showAlert("Prezzo Aggiornato", "Il prezzo intero è stato aggiornato con successo.");

            salvaPrezziSuFile();
            // Aggiorna la UI se necessario, ad esempio ricaricando i dati nella TableView
        } catch (NumberFormatException | FilmGiaPresenteException | DurataFilmNonValidaException |
                 TitoloVuotoException | FilmNonTrovatoException | SalaGiaEsistenteException | SalaNonTrovataException |
                 SovrapposizioneSpettacoloException | FilmNonValidoException | SalaNonValidaException |
                 SpettacoloNonTrovatoException e) {
            // Gestire l'eccezione se il valore inserito non è un numero
        }
    }

    private void impostaPrezzoRidotto() {
        try {
            double nuovoPrezzoRidotto = Double.parseDouble(impostaPrzRidotto_textflied.getText());
            ICommand cambiaPrezzoRidotto = new ImpostaPrezzoRidottoCommand(prezziBiglietto, nuovoPrezzoRidotto);
            amministratore.setCommand(cambiaPrezzoRidotto);
            amministratore.eseguiComando();
            aggiornaPrezziTableView();
            // Mostra un alert di successo
            showAlert("Prezzo Aggiornato", "Il prezzo ridotto è stato aggiornato con successo.");
            salvaPrezziSuFile();
            // Aggiorna la UI se necessario, ad esempio ricaricando i dati nella TableView
        } catch (NumberFormatException | FilmGiaPresenteException | DurataFilmNonValidaException |
                 TitoloVuotoException | FilmNonTrovatoException | SalaGiaEsistenteException | SalaNonTrovataException |
                 SovrapposizioneSpettacoloException | FilmNonValidoException | SalaNonValidaException |
                 SpettacoloNonTrovatoException e) {
            // Gestire l'eccezione se il valore inserito non è un numero
        }
    }

    private void impostaSovrapprezzo() {
        try {
            //double sovrapprezzo = Double.parseDouble(impostaSovraPrz_textflied1.getText());
            double sovrapprezzo = sovrapprezzo_spinner.getValue();
            ICommand impostaSovrapprezzo = new ImpostaSovrapprezzoCommand(prezziBiglietto, sovrapprezzo);
            amministratore.setCommand(impostaSovrapprezzo);
            amministratore.eseguiComando();
            aggiornaPrezziTableView();
            // Mostra un alert di successo
            showAlert("Prezzo Aggiornato", "Il sovrapprezzo è stato aggiornato con successo.");
            salvaPrezziSuFile();
            // Aggiorna la UI se necessario, ad esempio ricaricando i dati nella TableView
        } catch (NumberFormatException | FilmGiaPresenteException | DurataFilmNonValidaException |
                 TitoloVuotoException | FilmNonTrovatoException | SalaGiaEsistenteException | SalaNonTrovataException |
                 SovrapposizioneSpettacoloException | FilmNonValidoException | SalaNonValidaException |
                 SpettacoloNonTrovatoException e) {
            // Gestire l'eccezione se il valore inserito non è un numero
        }
    }

    private void salvaPrezziSuFile() {
        try {
            prezziBigliettoSerializer.serialize(prezziBiglietto, "prezziBiglietto.ser");
        } catch (Exception e) {
            // Gestire l'eccezione, ad esempio mostrando un messaggio di errore all'utente
        }
    }


    private void caricaPrezziDaFile() {
        File file = new File("prezziBiglietto.ser");
        if (file.exists()) {
            try {
                // Il file esiste, quindi deserializza i prezzi
                prezziBiglietto = (IPrezziBiglietto) prezziBigliettoSerializer.deserialize("prezziBiglietto.ser");
                // Aggiorna l'UI con i prezzi caricati, se necessario
            } catch (Exception e) {
                // Gestisce altre eccezioni durante il processo di deserializzazione
                // Potrebbe essere utile mostrare un messaggio di errore all'utente
            }
        } else {
            try {
                // Il file non esiste: crea un nuovo file con l'istanza corrente di prezziBiglietto
                // Assumi che prezziBiglietto sia già stato inizializzato altrove nel codice
                prezziBigliettoSerializer.serialize(prezziBiglietto, "prezziBiglietto.ser");
            } catch (Exception e) {
                // Gestisci l'eccezione di fallimento della serializzazione
                // Potrebbe essere utile mostrare un messaggio di errore all'utente
            }
        }
    }
}



        // Gestisce altri tipi di eccezioni durante la deserializzazione
        // Mostra un messaggio di errore o inizializza con valori di backup, se appropriato





