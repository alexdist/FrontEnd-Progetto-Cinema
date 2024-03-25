package org.example.interfaccia_grafica;

import Serializzazione.adapter.adaptee.PrezziBigliettoSerializer;
import Serializzazione.adapter.adaptee.SpettacoloSerializer;
import Serializzazione.adapter.adapter.PrezziBigliettoSerializerAdapter;
import Serializzazione.adapter.adapter.SpettacoloSerializerAdapter;
import Serializzazione.adapter.target.IDataSerializer;
import cinema_Infrastructure.spettacolo.ISpettacolo;
import domain.Utente;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.example.interfaccia_grafica.general_utility_classes.AlertUtil;
import org.example.interfaccia_grafica.general_utility_classes.serializzazione.ISpettacoloDataSerializer;
import org.example.interfaccia_grafica.general_utility_classes.serializzazione.SpettacoloDataSerializer;
import org.example.interfaccia_grafica.pagamento.PagamentoController;
import org.example.interfaccia_grafica.service_userdashcontroller.GestoreAcquisti;
import org.example.interfaccia_grafica.service_userdashcontroller.IGestoreAcquisti;
import org.example.interfaccia_grafica.service_userdashcontroller.IServizioPrezziBiglietto;
import org.example.interfaccia_grafica.service_userdashcontroller.ServizioPrezziBiglietto;
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


public class UserDashboardController {

    @FXML
    private TableColumn<ISpettacolo, String> posti_disponibili_tableview1;

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

    @FXML
    private ISpettacolo spettacoloSelezionato;

    @FXML
    private List<IBiglietto> bigliettiCreati = new ArrayList<>();

    @FXML
    private double x = 0;
    @FXML
    private double y = 0;

    // Nella classe UserDashboardController
    @FXML
    private Utente utente;

    @FXML
    IGeneratoreIDPersistente generatoreID;

    @FXML
    private IGestoreAcquisti gestoreAcquisti;

    @FXML
    private IServizioPrezziBiglietto prezziBigliettoDaFile;

    @FXML
    private ISpettacoloDataSerializer spettacoloDataSerializer;


    @FXML

    public void initialize(){
        prezziBigliettoDaFile = new ServizioPrezziBiglietto();

        spettacoloDataSerializer = new SpettacoloDataSerializer(new SpettacoloSerializerAdapter(new SpettacoloSerializer()));

        generatoreID = new GeneratoreIDPersistenteBiglietti();

        this.gestoreAcquisti = new GestoreAcquisti(generatoreID, prezziBigliettoDaFile.getPrezziBiglietto());

        spettacoli_user_tableview.refresh();

        // Configurazione dello Spinner per il numero di biglietti
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 30, 0);
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

        posti_disponibili_tableview1.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getSala().getPostiDisponibili() + " disponibili")
        );
        posti_disponibili_tableview1.setCellFactory(column -> new TableCell<ISpettacolo, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                // Pulisce lo stile precedente
                setText(null);
                setStyle("");

                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    // Estrai il numero di posti disponibili dal testo
                    String[] parts = item.split(" ");
                    int postiDisponibili = Integer.parseInt(parts[0]);

                    // Imposta il testo della cella
                    setText(item);

                    // Se i posti disponibili sono meno di 5, colora il testo di rosso
                    if (postiDisponibili < 5) {
                        setStyle("-fx-text-fill: red;");
                    } else {
                        setStyle("-fx-text-fill: black;"); // Oppure un altro colore per valori superiori
                    }
                }
            }
        });

        genereFilm_user_tableview.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFilm().getGenere()));


        List<ISpettacolo> spettacoli = spettacoloDataSerializer.caricaSpettacoli();
        spettacoli_user_tableview.setItems(FXCollections.observableList(spettacoli));

        // Aggiungi un listener alla selezione degli elementi nella TableView
        spettacoli_user_tableview.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                ISpettacolo spettacoloSelezionato = spettacoli_user_tableview.getSelectionModel().getSelectedItem();

                // Aggiorna le Label con i dati dello spettacolo selezionato
                aggiornaLabelConSpettacoloSelezionato(spettacoloSelezionato);
            }
        });
    }

    private void aggiornaPrezzoParziale() {

        if (numeroBiglietti_spinner.getValue() != null) {

            int numeroBiglietti = numeroBiglietti_spinner.getValue();
            // Ottieni i prezzi aggiornati dal servizio
            IPrezziBiglietto prezziAttuali = prezziBigliettoDaFile.getPrezziBiglietto();

            double prezzoApplicato = utente.getEta() < 14 ? prezziAttuali.getPrezzoRidotto() : prezziAttuali.getPrezzoIntero();
            double prezzoParziale = numeroBiglietti * prezzoApplicato;
            prezzoParziale_label.setText(String.format("%.2f€", prezzoParziale));
            prezzoTotale_label.setText(String.format("%.2f€", prezzoParziale));

        }
    }



    public void selezionaSpettacolo() {
        ISpettacolo spettacoloSelezionatoTemp = spettacoli_user_tableview.getSelectionModel().getSelectedItem();

        if (spettacoloSelezionatoTemp != null) {
            // Imposta lo spettacolo selezionato
            spettacoloSelezionato = spettacoloSelezionatoTemp;
            // Supponiamo che spettacoloSelezionato.getOrarioProiezione() ritorni un oggetto LocalDateTime
            LocalDateTime dataOrarioProiezione = spettacoloSelezionato.getOrarioProiezione();

            // Crea un oggetto DateTimeFormatter e specifica il formato desiderato
            DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            // Formatta la data
            String dataFormattata = dataOrarioProiezione.format(formatoData);

            // Imposta il testo formattato nel label
            selectDataFilm_label.setText(dataFormattata);

            // Aggiorna le label con i dettagli dello spettacolo
            selectTitoloFilm_label.setText(spettacoloSelezionato.getFilm().getTitolo());
            selectGenereFilm_label.setText(spettacoloSelezionato.getFilm().getGenere());


            // Chiama il metodo per applicare le strategie di prezzo al prezzo del biglietto
            prezziBigliettoDaFile.applicaStrategieDiPrezzo(spettacoloSelezionato);
            aggiornaPrezzoParziale();

            // Preparazione per il passo successivo (numero biglietti già impostato tramite Spinner)
        }
    }

    @FXML
    public void procediAcquisto(){
        List<IBiglietto> bigliettiCreati = gestoreAcquisti.procediAcquisto(spettacoloSelezionato, utente, numeroBiglietti_spinner.getValue());

        if (bigliettiCreati.isEmpty()) {
            AlertUtil.showInformationAlert("Impossibile procedere all'acquisto. Posti a sedere esauriti o numero biglietti selezionati maggiore del numero di posti a sedere disponibili.");
            return;
        }

        visualizzaInformazioniBiglietti();
        caricaSchermataPagamento(bigliettiCreati);
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

    public void caricaSchermataPagamento(List<IBiglietto> bigliettiCreati) {
        try {
            // Carica il nuovo contenuto FXML per il centro
            FXMLLoader loader = new FXMLLoader(getClass().getResource("pagamento.fxml")); // Assicurati di usare il percorso corretto
            AnchorPane pagamentoPane = loader.load();

            // Ottieni l'accesso al controller associato alla vista caricata
            PagamentoController pagamentoController = loader.getController();

            // Passa l'oggetto Utente e la lista dei biglietti al controller della schermata di pagamento
            pagamentoController.setUtente(this.utente);
            pagamentoController.setBigliettiDaAcquistare(bigliettiCreati); // Usa la lista dei biglietti passata come parametro

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
