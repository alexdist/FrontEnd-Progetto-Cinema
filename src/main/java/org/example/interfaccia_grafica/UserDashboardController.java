package org.example.interfaccia_grafica;

import Serializzazione.adapter.adaptee.PrezziBigliettoSerializer;
import Serializzazione.adapter.adaptee.SpettacoloSerializer;
import Serializzazione.adapter.adapter.PrezziBigliettoSerializerAdapter;
import Serializzazione.adapter.adapter.SpettacoloSerializerAdapter;
import Serializzazione.adapter.target.IDataSerializer;
import cinema_Infrastructure.spettacolo.ISpettacolo;
import domain.Utente;
import id_generator_factory.abstract_factory.GeneratoreIDFactory;
import id_generator_factory.concrete_factories.GeneratoreIDBigliettoFactory;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;

import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.example.interfaccia_grafica.general_utility_classes.AlertUtil;
import prova_id_PERSISTENTE.GeneratoreIDPersistenteBiglietti;
import prova_id_PERSISTENTE.IGeneratoreIDPersistente;
import ticket.factory.abstract_factory.BigliettoFactory;
import ticket.factory.concrete_factory.BigliettoInteroFactory;
import ticket.factory.concrete_factory.BigliettoRidottoFactory;
import ticket.factory.product.IBiglietto;
import ticket_pricing.IPrezziBiglietto;
import ticket_pricing.PrezziBiglietto;
import ticket_pricing.strategy.Context;
import ticket_pricing.strategy.PrezzoBaseStrategy;
import ticket_pricing.strategy.PrezzoWeekEndStrategy;

import java.util.List;



public class UserDashboardController {
    @FXML
    private Button close;

    @FXML
    private Button esci_btn;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private Button minimize;

    @FXML
    private AnchorPane topForm_anchorpane;

    @FXML
    private Label label_utente;

    @FXML
    private TableView<ISpettacolo> spettacoli_user_tableview;

    @FXML
    private TableColumn<ISpettacolo, LocalDateTime> data_user_tableview;

    @FXML
    private TableColumn<ISpettacolo, String> genereFilm_user_tableview;

    @FXML
    private TableColumn<ISpettacolo, String> sala_user_tableview;

    @FXML
    private TableColumn<ISpettacolo, String> titoloFilm_user_tableview;

    @FXML
    private Label selectDataFilm_label;

    @FXML
    private Label selectGenereFilm_label;

    @FXML
    private Label selectTitoloFilm_label;

    @FXML
    private Button select_button;

    @FXML
    private Spinner<Integer> numeroBiglietti_spinner;

    @FXML
    private Button procediAcquistoBigliettoButton;

    @FXML
    private Label prezzoParziale_label;

    @FXML
    private Label prezzoTotale_label;

    private ISpettacolo spettacoloSelezionato;

    private List<IBiglietto> bigliettiCreati = new ArrayList<>();

    @FXML
    private double x = 0;
    private double y = 0;

    // Nella classe UserDashboardController
    private Utente utente;


    // Aggiungi i campi per i prezzi dei biglietti
//    private double prezzoIntero;
//    private double prezzoRidotto;

   // private IPrezziBiglietto prezziBiglietto; // Istanza globale per gestire i prezzi dei biglietti
   // Deserializza gli spettacoli dal file
   IDataSerializer spettacoloSerializerAdapter = new SpettacoloSerializerAdapter();
   IDataSerializer prezziBigliettoSerializer = new PrezziBigliettoSerializerAdapter();

   // IPrezziBiglietto prezziBiglietto = new PrezziBiglietto(0,0);
    IGeneratoreIDPersistente generatoreID = new GeneratoreIDPersistenteBiglietti();
    IPrezziBiglietto prezziBigliettoBase;
    IPrezziBiglietto prezziBiglietto;
    @FXML

    public void initialize() {



        //carica i prezzi dei biglietti
        caricaPrezziBiglietti();

        // Configurazione dello Spinner per il numero di biglietti
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 30, 0); // Min: 1, Max: 100, Default: 1
        numeroBiglietti_spinner.setValueFactory(valueFactory);

        // Aggiungi un ChangeListener allo spinner dei numeri di biglietti
        numeroBiglietti_spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                aggiornaPrezzoParziale();
            }
        });

        // Configura i cellValueFactory per ogni colonna utilizzando i nomi specificati
        titoloFilm_user_tableview.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFilm().getTitolo()));

        sala_user_tableview.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getSala().getNumeroSala())));

        // Assumendo che la colonna "data_user_tableview" voglia visualizzare solo la data,
        // senza l'orario, o viceversa. Adattalo secondo le tue necessità
        data_user_tableview.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getOrarioProiezione()));
        // Imposta il Custom CellFactory per formattare l'orario di proiezione
        data_user_tableview.setCellFactory(column -> {
            return new TableCell<ISpettacolo, LocalDateTime>() {
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        // Formattazione della data e dell'orario
                        setText(item.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                    }
                }
            };
        });

        // Non hai fornito un esempio specifico per "genereFilm_user_tableview", quindi uso il titolo come placeholder
        // Sostituisci con la corretta proprietà per il genere, se disponibile
        genereFilm_user_tableview.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFilm().getGenere())); // Assumi che IFilm abbia un metodo getGenere()




        Object result = spettacoloSerializerAdapter.deserialize("spettacoli.ser");
        if (result instanceof List<?>) {
            List<?> resultList = (List<?>) result;
            if (!resultList.isEmpty() && resultList.get(0) instanceof ISpettacolo) {
                List<ISpettacolo> spettacoli = (List<ISpettacolo>) resultList;
                spettacoli_user_tableview.setItems(FXCollections.observableList(spettacoli));
            } else {
                System.out.println("La lista deserializzata non contiene elementi di tipo ISpettacolo");
            }
        } else {
            System.out.println("Il risultato della deserializzazione non è una lista");
        }

        // Assumi che la tua TableView sia già configurata qui

        // Aggiungi un listener alla selezione degli elementi nella TableView
        spettacoli_user_tableview.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                ISpettacolo spettacoloSelezionato = spettacoli_user_tableview.getSelectionModel().getSelectedItem();

                // Aggiorna le Label con i dati dello spettacolo selezionato
                aggiornaLabelConSpettacoloSelezionato(spettacoloSelezionato);
            }
        });

    }

//    private void caricaPrezziBiglietti() {
//        //IDataSerializer prezziBigliettoSerializer = new PrezziBigliettoSerializerAdapter();
//
//        File file = new File("prezziBiglietto.ser");
//        if (file.exists()) {
//            try {
//                prezziBiglietto = (IPrezziBiglietto) prezziBigliettoSerializer.deserialize("prezziBiglietto.ser");
//            } catch (Exception e) {
//                // Gestione dell'errore
//            }
//        } else {
//            // Gestione del caso in cui il file non esiste
//        }
//    }

    private void caricaPrezziBiglietti() {
        File file = new File("prezziBiglietto.ser");
        if (file.exists()) {
            try {
                // Deserializza i prezzi del biglietto dal file
                IPrezziBiglietto prezziDaFile = (IPrezziBiglietto) prezziBigliettoSerializer.deserialize("prezziBiglietto.ser");

                // Inizializza sia prezziBigliettoBase che prezziBiglietto con i valori deserializzati
                prezziBigliettoBase = new PrezziBiglietto(prezziDaFile.getPrezzoIntero(), prezziDaFile.getPrezzoRidotto(), prezziDaFile.getSovrapprezzo());
                prezziBiglietto = new PrezziBiglietto(prezziDaFile.getPrezzoIntero(), prezziDaFile.getPrezzoRidotto(), prezziDaFile.getSovrapprezzo());
            } catch (Exception e) {
                // Gestione dell'errore
            }
        } else {
            // Gestione del caso in cui il file non esiste
        }
    }

    private void aggiornaPrezzoParziale() {
        if (numeroBiglietti_spinner.getValue() != null) {
            int numeroBiglietti = numeroBiglietti_spinner.getValue();
            double prezzoApplicato = utente.getEta() < 14 ? prezziBiglietto.getPrezzoRidotto() : prezziBiglietto.getPrezzoIntero();
            double prezzoParziale = numeroBiglietti * prezzoApplicato;
            prezzoParziale_label.setText(String.format("%.2f€", prezzoParziale));
            prezzoTotale_label.setText(String.format("%.2f€", prezzoParziale));
        }
    }

//    public void selezionaSpettacolo() {
//        spettacoloSelezionato = spettacoli_user_tableview.getSelectionModel().getSelectedItem();
//        if (spettacoloSelezionato != null) {
//            selectTitoloFilm_label.setText(spettacoloSelezionato.getFilm().getTitolo());
//            selectGenereFilm_label.setText(spettacoloSelezionato.getFilm().getGenere());
//            selectDataFilm_label.setText(spettacoloSelezionato.getOrarioProiezione().toString());
//            // Applica le strategie di prezzo e aggiorna l'interfaccia utente
//            applicaStrategieDiPrezzo();
//            // Preparazione per il passo successivo (numero biglietti già impostato tramite Spinner)
//        }
//    }

    public void selezionaSpettacolo() {
        ISpettacolo spettacoloSelezionatoTemp = spettacoli_user_tableview.getSelectionModel().getSelectedItem();
        if (spettacoloSelezionatoTemp != null) {
            // Imposta lo spettacolo selezionato
            spettacoloSelezionato = spettacoloSelezionatoTemp;

            // Aggiorna le label con i dettagli dello spettacolo
            selectTitoloFilm_label.setText(spettacoloSelezionato.getFilm().getTitolo());
            selectGenereFilm_label.setText(spettacoloSelezionato.getFilm().getGenere());
            selectDataFilm_label.setText(spettacoloSelezionato.getOrarioProiezione().toString());

            // Reset dei prezzi ai valori base prima di applicare le strategie
            resetPrezziBiglietto();

            // Applica le strategie di prezzo
            applicaStrategieDiPrezzo();

            // Preparazione per il passo successivo (numero biglietti già impostato tramite Spinner)
        }
    }



//    private void applicaStrategieDiPrezzo() {
//        if (spettacoloSelezionato != null) {
//            Context context = new Context(new PrezzoBaseStrategy(prezziBiglietto));
//            context.executeStrategy();
//
//            // Verifica se la data dello spettacolo cade in un weekend e applica l'aumento
//            DayOfWeek giorno = spettacoloSelezionato.getOrarioProiezione().getDayOfWeek();
//            if (giorno == DayOfWeek.SATURDAY || giorno == DayOfWeek.SUNDAY) {
//                context.setStrategy(new PrezzoWeekEndStrategy(prezziBiglietto, prezziBiglietto.getSovrapprezzo())); // 50% di aumento
//                context.executeStrategy();
//            }
//
//            // Aggiorna i prezzi visualizzati all'utente
//            aggiornaPrezzoParziale();
//        }
//    }

    private void applicaStrategieDiPrezzo() {
        // Assicurati che spettacoloSelezionato non sia null
        if (spettacoloSelezionato != null) {
            // Crea un nuovo contesto con i prezzi base
            Context context = new Context(new PrezzoBaseStrategy(prezziBiglietto));

            // Esegue la strategia base per assicurare che i prezzi siano inizializzati correttamente
            context.executeStrategy();

            // Verifica condizioni specifiche e applica strategie aggiuntive
            DayOfWeek giorno = spettacoloSelezionato.getOrarioProiezione().getDayOfWeek();
            if (giorno == DayOfWeek.SATURDAY || giorno == DayOfWeek.SUNDAY) {
                // Sovrapprezzo weekend
                context.setStrategy(new PrezzoWeekEndStrategy(prezziBiglietto, calcolaSovrapprezzoWeekend()));
                context.executeStrategy();
            }

            // Aggiorna i prezzi visualizzati all'utente
            aggiornaPrezzoParziale();
        }
    }

    private void resetPrezziBiglietto() {
        // Reset dei prezzi ai valori iniziali
        prezziBiglietto.setPrezzoIntero(prezziBigliettoBase.getPrezzoIntero());
        prezziBiglietto.setPrezzoRidotto(prezziBigliettoBase.getPrezzoRidotto());
        prezziBiglietto.setSovrapprezzo(0); // Assicurati che questa proprietà esista e sia gestibile adeguatamente
    }

    private double calcolaSovrapprezzoWeekend() {
        // Qui puoi calcolare il sovrapprezzo specifico per il weekend se variabile
        return prezziBigliettoBase.getSovrapprezzo(); // Modifica con il tuo metodo di calcolo
    }


    @FXML
    public void procediAcquisto() {
        if (spettacoloSelezionato == null) {
            System.out.println("Seleziona uno spettacolo prima di procedere.");
            return;
        }

        // Ottiene il numero di biglietti selezionato dallo Spinner
        int numeroBiglietti = numeroBiglietti_spinner.getValue();




        // Determina il tipo di biglietto in base all'età dell'utente
        BigliettoFactory bigliettoFactory;
        if (utente.getEta() < 14) {
            bigliettoFactory = new BigliettoRidottoFactory(generatoreID);
        } else {
            bigliettoFactory = new BigliettoInteroFactory(generatoreID);
        }
        
        double prezzoApplicato = utente.getEta() < 14 ? prezziBiglietto.getPrezzoRidotto() : prezziBiglietto.getPrezzoIntero();

        for (int i = 0; i < numeroBiglietti; i++) {
            IBiglietto biglietto = bigliettoFactory.creaBiglietto(spettacoloSelezionato, utente, prezzoApplicato);
            bigliettiCreati.add(biglietto);
        }

        visualizzaInformazioniBiglietti();
        caricaSchermataPagamento();
    }

    private void visualizzaInformazioniBiglietti() {
        for (IBiglietto biglietto : bigliettiCreati) {
            Utente acquirente = biglietto.getAcquirente();
            System.out.println("Biglietto ID: " + biglietto.getId());
            System.out.println("Nome: " + acquirente.getNome());
            System.out.println("Cognome: " + acquirente.getCognome());
            System.out.println("Prezzo: " + biglietto.getCosto() + "€");
            // Aggiungi eventuali altre informazioni che desideri visualizzare
            System.out.println("-------------------------------------");
        }
    }



    public void caricaSchermataPagamento() {
        try {
            // Carica il nuovo contenuto FXML per il centro
            FXMLLoader loader = new FXMLLoader(getClass().getResource("pagamento.fxml")); // Assicurati di usare il percorso corretto
            AnchorPane pagamentoPane = loader.load();

            // Ottieni l'accesso al controller associato alla vista caricata
            PagamentoController pagamentoController = loader.getController();
            // Passa l'oggetto Utente al controller
            pagamentoController.setUtente(this.utente);
            pagamentoController.setBigliettiDaAcquistare(this.bigliettiCreati); // Passa l'elenco dei biglietti

            // Sostituisci il contenuto del centro nel BorderPane
            mainBorderPane.setCenter(pagamentoPane);
        } catch (IOException e) {
            e.printStackTrace(); // Gestisci l'eccezione come preferisci
        }
    }



    private void aggiornaLabelConSpettacoloSelezionato(ISpettacolo spettacolo) {
        // Assumi che la classe ISpettacolo e le sue relative classi abbiano i metodi getter corretti
        selectTitoloFilm_label.setText(spettacolo.getFilm().getTitolo());
        selectGenereFilm_label.setText(spettacolo.getFilm().getGenere());
        selectDataFilm_label.setText(spettacolo.getOrarioProiezione().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
        updateLabelUtente();
        // Aggiornare la vista della dashboard, se necessario
    }

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

    // Metodo helper per aggiornare la label con il nome e cognome dell'utente
    private void updateLabelUtente() {
        if (utente != null && label_utente != null) {
            String nome = utente.getNome(); // Assumi che la classe Utente abbia il metodo getNome()
            String cognome = utente.getCognome(); // Assumi che la classe Utente abbia il metodo getCognome()
            label_utente.setText(nome + " " + cognome);
        }
    }



    public void closeDashboard(){

        System.exit(0);
    }

    public void minimizeDashboard(){

        Stage stage = (Stage)topForm_anchorpane.getScene().getWindow();
        stage.setIconified(true);
    }
}
