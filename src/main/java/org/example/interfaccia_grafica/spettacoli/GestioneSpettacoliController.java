package org.example.interfaccia_grafica.spettacoli;

import Serializzazione.adapter.adaptee.FilmSerializer;
import Serializzazione.adapter.adaptee.SalaSerializer;
import Serializzazione.adapter.adaptee.SpettacoloSerializer;
import Serializzazione.adapter.adapter.FilmSerializerAdapter;
import Serializzazione.adapter.adapter.SalaSerializerAdapter;
import Serializzazione.adapter.adapter.SpettacoloSerializerAdapter;
import Serializzazione.adapter.target.IDataSerializer;
import admin_commands.spettacolo.aggiungi_spettacolo.AggiungiSpettacoloCommand;
import admin_commands.spettacolo.modifica_spettacolo.modifica_film.ModificaFilmPerIdSpettacoloCommand;
import admin_commands.spettacolo.modifica_spettacolo.modifica_orario.ModificaOrarioPerIdSpettacoloCommand;
import admin_commands.spettacolo.modifica_spettacolo.modifica_sala.ModificaSalaPerIdSpettacoloCommand;
import admin_commands.spettacolo.rimuovi_spettacolo.RimuoviSpettacoloCommand;
import admin_interfaces.ICommand;
import cinema_Infrastructure.film.IFilm;
import cinema_Infrastructure.sala.ISala;
import cinema_Infrastructure.spettacolo.ISpettacolo;
import cinema_Infrastructure.spettacolo.Spettacolo;
import cinema_Infrastructure.spettacolo.gestione_spettacolo.*;
import domain.Amministratore;
import domain.Ruolo;
import exception.film.*;
import exception.sala.SalaGiaEsistenteException;
import exception.sala.SalaNonTrovataException;
import exception.sala.SalaNonValidaException;
import exception.spettacolo.SovrapposizioneSpettacoloException;
import exception.spettacolo.SpettacoloNonTrovatoException;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.example.interfaccia_grafica.spettacoli.utility_classes.AlertUtil;
import org.example.interfaccia_grafica.spettacoli.utility_classes.ComboBoxListenerUtil;
import org.example.interfaccia_grafica.spettacoli.utility_classes.ComboBoxUtil;
import org.example.interfaccia_grafica.spettacoli.utility_classes.DateTimeUtil;
import prova_id_PERSISTENTE.GeneratoreIDPersistenteSpettacolo;
import prova_id_PERSISTENTE.IGeneratoreIDPersistente;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GestioneSpettacoliController implements Initializable {
    @FXML
    private TextField IDRimuoviSpett_textfield;//

    @FXML
    private TableColumn<ISpettacolo, Long> IDSpettacoloCol_tableview;

    @FXML
    private TextField IDmodFilmSpett_textfield;

    @FXML
    private TextField IDmodOrarioSpett_textfield;

    @FXML
    private TextField IDmodSalaSpett_textfield;

    @FXML
    private AnchorPane aggElimSpett_anchorpane;

    @FXML
    private TextField aggFilmSpett_textfield;

    @FXML
    private TextField aggOrarioSpett_textfield;

    @FXML
    private TextField aggSalaSpett_textfield;

    @FXML
    private Button aggiungiSpettacolo_btn;

    @FXML
    private Button eliminaSpettacolo_btn;

    @FXML
    private TableColumn<ISpettacolo, String> filmCol_tableview;

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
    private TableColumn<ISpettacolo, LocalDateTime> orarioProiezioneCol_tableview;

    @FXML
    private TableColumn<ISpettacolo, String> salaCol_tableview;

    @FXML
    private TableColumn<ISpettacolo, String> spettacoloCol_tableview;

    @FXML
    private TableView<ISpettacolo> spettacolo_tableview;

    @FXML
    private Button switchModificaSpett_btn;

    @FXML
    private Button switchTornaIndietroSpett_btn;

    @FXML
    private ComboBox<IFilm> combobox_film;

    @FXML
    private ComboBox<ISala> combobox_sala;

    @FXML
    private ComboBox<IFilm> combobox_film_modifica;

    @FXML
    private ComboBox<ISala> combobox_sala_modifica;

    @FXML
    private DatePicker datePicker_modifica;

    @FXML
    private ComboBox<Integer> hoursComboBox_modifica;

    @FXML
    private ComboBox<Integer> minutesComboBox_modifica;

    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<Integer> hoursComboBox;
    @FXML
    private ComboBox<Integer> minutesComboBox;


    private ObservableList<ISpettacolo> spettacoliObservableList = FXCollections.observableArrayList();

    private List<ISpettacolo> spettacoli;

    private ObservableList<IFilm> filmObservableList = FXCollections.observableArrayList();
    private ObservableList<ISala> salaObservableList = FXCollections.observableArrayList();

    SpettacoloSerializer spettacoloSerializer = new SpettacoloSerializer();
    IDataSerializer spettacoloSerializerAdapter = new SpettacoloSerializerAdapter(spettacoloSerializer);

    FilmSerializer filmSerializer = new FilmSerializer();
    IDataSerializer filmSerializerAdapter = new FilmSerializerAdapter(filmSerializer);

    // Adapter instances
    SalaSerializer salaSerializer = new SalaSerializer();
    IDataSerializer salaSerializerAdapter = new SalaSerializerAdapter(salaSerializer);


    @Override
    public void initialize(URL location, ResourceBundle resources){

        try {
            spettacoli = (List<ISpettacolo>) spettacoloSerializerAdapter.deserialize("spettacoli.ser");
            spettacoliObservableList.addAll(spettacoli);
        } catch (Exception e) {
            System.out.println("Impossibile caricare gli spettacoli esistenti. " + e.getMessage());
            spettacoli = new ArrayList<>();
        }

        hoursComboBox.setItems(FXCollections.observableArrayList(IntStream.rangeClosed(0, 23).boxed().collect(Collectors.toList())));
       // minutesComboBox.setItems(FXCollections.observableArrayList(IntStream.rangeClosed(0, 59).boxed().collect(Collectors.toList())));
        minutesComboBox.setItems(FXCollections.observableArrayList(
                IntStream.rangeClosed(0, 59)
                        .filter(i -> i % 5 == 0)
                        .boxed()
                        .collect(Collectors.toList())
        ));

        hoursComboBox_modifica.setItems(FXCollections.observableArrayList(IntStream.rangeClosed(0, 23).boxed().collect(Collectors.toList())));
        //minutesComboBox_modifica.setItems(FXCollections.observableArrayList(IntStream.rangeClosed(0, 59).boxed().collect(Collectors.toList())));
        minutesComboBox_modifica.setItems(FXCollections.observableArrayList(
                IntStream.rangeClosed(0, 59)
                        .filter(i -> i % 5 == 0)
                        .boxed()
                        .collect(Collectors.toList())
        ));

        // Imposta un valore predefinito se necessario
        hoursComboBox.getSelectionModel().select(LocalTime.now().getHour());
        minutesComboBox.getSelectionModel().select(LocalTime.now().getMinute());

        // Popola le ObservableList con i dati di film e sale.
        // Assumi che i metodi getFilms() e getSale() restituiscano le liste necessarie.
        filmObservableList.addAll(getFilms());
        salaObservableList.addAll(getSale());

        // Configura ComboBox e TableView con le utilità
        ComboBoxUtil.setupFilmComboBox(combobox_film, filmObservableList);
        ComboBoxUtil.setupSalaComboBox(combobox_sala, salaObservableList);

        ComboBoxUtil.setupFilmComboBox(combobox_film_modifica,filmObservableList);
        ComboBoxUtil.setupSalaComboBox(combobox_sala_modifica,salaObservableList);

        // Aggiungi listener ai ComboBox utilizzando la classe di utilità
        ComboBoxListenerUtil.addFilmSelectionListener(combobox_film);
        ComboBoxListenerUtil.addSalaSelectionListener(combobox_sala);

        // Popola combobox_film_modifica con gli oggetti film
        combobox_film_modifica.setItems(filmObservableList);


        // Aggiungi listener ai ComboBox utilizzando la classe di utilità
        ComboBoxListenerUtil.addFilmSelectionListener(combobox_film_modifica);
        ComboBoxListenerUtil.addSalaSelectionListener(combobox_sala_modifica);

        spettacoloCol_tableview.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFilm().getTitolo()));

        filmCol_tableview.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFilm().getTitolo()));

        salaCol_tableview.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getSala().getNumeroSala())));

        orarioProiezioneCol_tableview.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getOrarioProiezione()));

        // Imposta il Custom CellFactory per formattare l'orario di proiezione
        orarioProiezioneCol_tableview.setCellFactory(column -> {
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

        IDSpettacoloCol_tableview.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getId()));

        // Assicurati di impostare la `TableView` con la tua `ObservableList`
        spettacolo_tableview.setItems(spettacoliObservableList);
    }


    @FXML
    public void aggiungiSpettacolo() throws FilmGiaPresenteException, DurataFilmNonValidaException, SpettacoloNonTrovatoException, TitoloVuotoException, SalaGiaEsistenteException, SalaNonTrovataException, FilmNonValidoException, SovrapposizioneSpettacoloException, SalaNonValidaException, FilmNonTrovatoException {

        IFilm filmSelezionato = combobox_film.getSelectionModel().getSelectedItem();
        ISala salaSelezionata = combobox_sala.getSelectionModel().getSelectedItem();
        // Assumendo che 'datePicker', 'hoursComboBox', e 'minutesComboBox' siano i tuoi componenti UI nel controller
        LocalDateTime orarioSelezionato = DateTimeUtil.getSelectedDateTime(datePicker, hoursComboBox, minutesComboBox); // Metodo che combina DatePicker e ComboBox

        if (filmSelezionato == null || salaSelezionata == null || orarioSelezionato == null) {
            AlertUtil.showErrorAlert("Devi selezionare un film, una sala e un orario per aggiungere uno spettacolo.");
            return;
        }

        // Qui si potrebbe implementare la logica per generare l'ID dello spettacolo,
        // ad esempio con un IGeneratoreIDPersistente come hai fatto nel GestioneFilmController

        IGeneratoreIDPersistente generatoreIDSpettacolo = new GeneratoreIDPersistenteSpettacolo();

        IAggiungiSpettacolo servizioAggiungiSpettacolo = new AggiungiSpettacolo(spettacoli, generatoreIDSpettacolo);

        ISpettacolo nuovoSpettacolo = new Spettacolo(filmSelezionato, salaSelezionata, orarioSelezionato);

        ICommand aggiungiSpettacoloCommand = new AggiungiSpettacoloCommand(servizioAggiungiSpettacolo, nuovoSpettacolo);

        Amministratore amministratore = new Amministratore("Nome", "Cognome", Ruolo.AMMINISTRATORE);
        amministratore.setCommand(aggiungiSpettacoloCommand);
        amministratore.eseguiComando();
        aggiungiSpettacoloTable(nuovoSpettacolo);
        spettacoloSerializerAdapter.serialize(spettacoli, "spettacoli.ser");
        AlertUtil.showInformationAlert("Spettacolo aggiunto con successo!");
    }

    @FXML
    private void modificaOrario(){
        try {
            // Estrai l'ID dello spettacolo da modificare dal TextField
            long idSpettacolo = Long.parseLong(IDmodOrarioSpett_textfield.getText().trim());

            // Controlla se la data è stata selezionata
            LocalDate data = datePicker_modifica.getValue();
            if (data == null) {
                AlertUtil.showAlert("Errore", "Devi selezionare una data.");
                return;
            }

            // Estrai l'ora e i minuti dai ComboBox
            Integer ora = hoursComboBox_modifica.getSelectionModel().getSelectedItem();
            Integer minuti = minutesComboBox_modifica.getSelectionModel().getSelectedItem();
            if (ora == null || minuti == null) {
                AlertUtil.showAlert("Errore", "Devi selezionare sia l'ora che i minuti.");
                return;
            }

            // Costruisci l'oggetto LocalDateTime combinando data, ora e minuti
            LocalDateTime orarioModificato = LocalDateTime.of(data, LocalTime.of(ora, minuti));

            // Utilizza il servizio di modifica per aggiornare l'orario dello spettacolo
            IModificaSpettacolo servizioModificaOrarioSpettacolo = new ModificaSpettacolo(spettacoli);
            ICommand modificaOrarioSpettacoloCommand = new ModificaOrarioPerIdSpettacoloCommand(servizioModificaOrarioSpettacolo, idSpettacolo, orarioModificato);

            Amministratore amministratore = new Amministratore("Mario", "Rossi", Ruolo.AMMINISTRATORE); // Assumi che l'amministratore sia già definito
            amministratore.setCommand(modificaOrarioSpettacoloCommand);
            amministratore.eseguiComando();
            spettacoloSerializerAdapter.serialize(spettacoli, "spettacoli.ser");

            // Mostra un messaggio di successo
            AlertUtil.showAlert("Successo", "Orario dello spettacolo modificato con successo.");


            // Questo forza la TableView a aggiornarsi
            spettacolo_tableview.refresh();


        } catch (NumberFormatException e) {
            // Gestisce il caso in cui l'ID non sia un numero valido
            AlertUtil.showAlert("Errore di formato", "L'ID inserito non è valido.");
        } catch (Exception e) {
            // Gestisce eventuali altri errori
            AlertUtil.showAlert("Errore", "Errore durante la modifica dell'orario dello spettacolo: " + e.getMessage());
        }
    }


    @FXML
    private void modificaSala(){
        try{
            long idSpettacolo = Long.parseLong(IDmodSalaSpett_textfield.getText().trim());
            ISala salaSelezionata = combobox_sala_modifica.getSelectionModel().getSelectedItem();

            if(salaSelezionata == null){
                AlertUtil.showAlert("Errore", "Seleziona una sala da assegnare allo spettacolo.");
                return;
            }
            // Prepara il comando per la modifica dello spettacolo
            IModificaSpettacolo servizioModificaSpettacolo = new ModificaSpettacolo(spettacoli);
            Amministratore amministratore = new Amministratore("Mario", "Rossi", Ruolo.AMMINISTRATORE);
            ICommand modificaSalaSpettacoloCommand = new ModificaSalaPerIdSpettacoloCommand(servizioModificaSpettacolo, idSpettacolo, salaSelezionata);
            amministratore.setCommand(modificaSalaSpettacoloCommand);
            amministratore.eseguiComando();
            spettacoloSerializerAdapter.serialize(spettacoli, "spettacoli.ser");
            // Mostra un messaggio di successo
            AlertUtil.showAlert("Successo", "Sala dello spettacolo modificato con successo.");
            // Questo forza la TableView a aggiornarsi
            spettacolo_tableview.refresh();
        } catch (NumberFormatException e) {
            // Gestisce il caso in cui l'ID non sia un numero valido
            AlertUtil.showAlert("Errore di formato", "L'ID inserito non è valido.");
        } catch (Exception e) {
            // Gestisce eventuali altri errori
            AlertUtil.showAlert("Errore", "Errore durante la modifica dello spettacolo: " + e.getMessage());
        }
    }

    @FXML
    private void modificaFilm() {
        try {
            long idSpettacolo = Long.parseLong(IDmodFilmSpett_textfield.getText().trim()); // Converte l'ID in long
            IFilm filmSelezionato = combobox_film_modifica.getSelectionModel().getSelectedItem(); // Recupera il film selezionato

            if (filmSelezionato == null) {
                // Mostra un messaggio di errore se nessun film è stato selezionato
                AlertUtil.showAlert("Errore", "Seleziona un film da assegnare allo spettacolo.");
                return;
            }

            // Prepara il comando per la modifica dello spettacolo
            IModificaSpettacolo servizioModificaSpettacolo = new ModificaSpettacolo(spettacoli);
            Amministratore amministratore = new Amministratore("Mario", "Rossi", Ruolo.AMMINISTRATORE);
            ICommand modificaFilmSpettacoloCommand = new ModificaFilmPerIdSpettacoloCommand(servizioModificaSpettacolo, idSpettacolo, filmSelezionato);
            amministratore.setCommand(modificaFilmSpettacoloCommand);
            amministratore.eseguiComando();
            spettacoloSerializerAdapter.serialize(spettacoli, "spettacoli.ser");

            // Mostra un messaggio di successo
            AlertUtil.showAlert("Successo", "Film dello spettacolo modificato con successo.");
            // Questo forza la TableView a aggiornarsi
            spettacolo_tableview.refresh();

        } catch (NumberFormatException e) {
            // Gestisce il caso in cui l'ID non sia un numero valido
            AlertUtil.showAlert("Errore di formato", "L'ID inserito non è valido.");
        } catch (Exception e) {
            // Gestisce eventuali altri errori
            AlertUtil.showAlert("Errore", "Errore durante la modifica dello spettacolo: " + e.getMessage());
        }
    }


    public void aggiungiSpettacoloTable(ISpettacolo nuovoSpettacolo) {
        spettacoliObservableList.add(nuovoSpettacolo);
    }

    @FXML
    public void rimuoviSpettacolo() {
        try {
            long idSpettacolo = Long.parseLong(IDRimuoviSpett_textfield.getText());

            // Qui assumiamo che tu abbia definito un'interfaccia IRimuoviSpettacolo e la sua implementazione
            IRimuoviSpettacolo servizioRimuoviSpettacolo = new RimuoviSpettacolo(spettacoli);
            ICommand rimuoviSpettacoloCommand = new RimuoviSpettacoloCommand(servizioRimuoviSpettacolo, idSpettacolo);

            Amministratore amministratore = new Amministratore("Nome", "Cognome", Ruolo.AMMINISTRATORE);
            amministratore.setCommand(rimuoviSpettacoloCommand);
            amministratore.eseguiComando();

            // Aggiorna l'interfaccia utente e la persistenza
            rimuoviSpettacoloTable(idSpettacolo);
            AlertUtil.showInformationAlert("Spettacolo rimosso con successo!");

            // Opzionalmente, salva la lista aggiornata di spettacoli
            spettacoloSerializerAdapter.serialize(spettacoli, "spettacoli.ser");

        } catch (NumberFormatException e) {
            AlertUtil.showErrorAlert("L'ID inserito non è valido.");
        } catch (SpettacoloNonTrovatoException e) {
            AlertUtil.showErrorAlert("Spettacolo non trovato.");
        } catch (Exception e) {
            AlertUtil.showErrorAlert("Errore imprevisto durante la rimozione dello spettacolo: " + e.getMessage());
        }
    }

    private void rimuoviSpettacoloTable(long idSpettacoloDaRimuovere) {
        ISpettacolo spettacoloDaRimuovere = spettacoliObservableList.stream()
                .filter(spettacolo -> spettacolo.getId() == idSpettacoloDaRimuovere)
                .findFirst()
                .orElse(null);
        if (spettacoloDaRimuovere != null) {
            spettacoliObservableList.remove(spettacoloDaRimuovere);
            spettacoli.remove(spettacoloDaRimuovere);
        }
    }



    private List<IFilm> getFilms() {
        // Questo metodo deve recuperare l'elenco dei film, ad esempio:
        try {
            return (List<IFilm>) filmSerializerAdapter.deserialize("film.ser");
        } catch (Exception e) {
            // gestire l'eccezione e ritornare una lista vuota o gestire di conseguenza
            return new ArrayList<>();
        }
    }

    private List<ISala> getSale() {
        // Questo metodo deve recuperare l'elenco delle sale, simile a come viene fatto per i film
        try {
            return (List<ISala>) salaSerializerAdapter.deserialize("sale.ser");
        } catch (Exception e) {
            // gestire l'eccezione e ritornare una lista vuota o gestire di conseguenza
            return new ArrayList<>();
        }
    }



    @FXML
    public void switchSpettacolo(ActionEvent event){
        if(event.getSource() == switchModificaSpett_btn){
            aggElimSpett_anchorpane.setVisible(false);
            modSpettacolo_anchorpane.setVisible(true);

        }else if(event.getSource() == switchTornaIndietroSpett_btn){
            aggElimSpett_anchorpane.setVisible(true);
            modSpettacolo_anchorpane.setVisible(false);
        }
    }

}
