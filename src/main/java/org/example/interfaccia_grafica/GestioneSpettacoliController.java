package org.example.interfaccia_grafica;

import Serializzazione.adapter.adaptee.FilmSerializer;
import Serializzazione.adapter.adaptee.SalaSerializer;
import Serializzazione.adapter.adaptee.SpettacoloSerializer;
import Serializzazione.adapter.adapter.FilmSerializerAdapter;
import Serializzazione.adapter.adapter.SalaSerializerAdapter;
import Serializzazione.adapter.adapter.SpettacoloSerializerAdapter;
import Serializzazione.adapter.target.IDataSerializer;
import admin_commands.film.aggiungi_film.AggiungiFilmCommand;
import admin_commands.spettacolo.aggiungi_spettacolo.AggiungiSpettacoloCommand;
import admin_commands.spettacolo.rimuovi_spettacolo.RimuoviSpettacoloCommand;
import admin_interfaces.ICommand;
import cinema_Infrastructure.film.IFilm;
import cinema_Infrastructure.film.gestione_film.AggiungiFilm;
import cinema_Infrastructure.film.gestione_film.IAggiungiFilm;
import cinema_Infrastructure.sala.ISala;
import cinema_Infrastructure.spettacolo.ISpettacolo;
import cinema_Infrastructure.spettacolo.Spettacolo;
import cinema_Infrastructure.spettacolo.gestione_spettacolo.AggiungiSpettacolo;
import cinema_Infrastructure.spettacolo.gestione_spettacolo.IAggiungiSpettacolo;
import cinema_Infrastructure.spettacolo.gestione_spettacolo.IRimuoviSpettacolo;
import cinema_Infrastructure.spettacolo.gestione_spettacolo.RimuoviSpettacolo;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import prova_id_PERSISTENTE.GeneratoreIDPersistenteFilm;
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
    private TextField IDRimuoviSpett_textfield;

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
    private TableColumn<?, ?> spettacoloCol_tableview;

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
        minutesComboBox.setItems(FXCollections.observableArrayList(IntStream.rangeClosed(0, 59).boxed().collect(Collectors.toList())));

        // Imposta un valore predefinito se necessario
        hoursComboBox.getSelectionModel().select(LocalTime.now().getHour());
        minutesComboBox.getSelectionModel().select(LocalTime.now().getMinute());

        // Popola le ObservableList con i dati di film e sale.
        // Assumi che i metodi getFilms() e getSale() restituiscano le liste necessarie.
        filmObservableList.addAll(getFilms());
        salaObservableList.addAll(getSale());

        // Imposta il ComboBox con gli oggetti film.
        combobox_film.setItems(filmObservableList);
        combobox_film.setCellFactory((comboBox) -> {
            return new ListCell<IFilm>() {
                @Override
                protected void updateItem(IFilm item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getTitolo());
                    }
                }
            };
        });
        combobox_film.setButtonCell(
                new ListCell<IFilm>() {
                    @Override
                    protected void updateItem(IFilm item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText("Seleziona Film");
                        } else {
                            setText(item.getTitolo());
                        }
                    }
                }
        );

        // Imposta il ComboBox con gli oggetti sala.
        combobox_sala.setItems(salaObservableList);
        combobox_sala.setCellFactory((comboBox) -> {
            return new ListCell<ISala>() {
                @Override
                protected void updateItem(ISala item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(String.valueOf(item.getNumeroSala()));
                    }
                }
            };
        });
        combobox_sala.setButtonCell(
                new ListCell<ISala>() {
                    @Override
                    protected void updateItem(ISala item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText("Seleziona Sala");
                        } else {
                            setText(String.valueOf(item.getNumeroSala()));
                        }
                    }
                }
        );

        // Aggiungi i listener qui, dopo aver configurato i ComboBox
        combobox_film.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Logica da eseguire quando un film è selezionato
                System.out.println("Film selezionato: " + newValue.getTitolo());
            }
        });

        combobox_sala.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Logica da eseguire quando una sala è selezionata
                System.out.println("Sala selezionata: " + newValue.getNumeroSala());
            }
        });

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


    public LocalDateTime getSelectedDateTime() {
        LocalDate date = datePicker.getValue();
        Integer hour = hoursComboBox.getValue();
        Integer minute = minutesComboBox.getValue();
        if (date != null && hour != null && minute != null) {
            return LocalDateTime.of(date, LocalTime.of(hour, minute));
        } else {
            // Gestisci il caso in cui una selezione è nulla
            return null;
        }
    }

    @FXML
    public void aggiungiSpettacolo() throws FilmGiaPresenteException, DurataFilmNonValidaException, SpettacoloNonTrovatoException, TitoloVuotoException, SalaGiaEsistenteException, SalaNonTrovataException, FilmNonValidoException, SovrapposizioneSpettacoloException, SalaNonValidaException, FilmNonTrovatoException {

        IFilm filmSelezionato = combobox_film.getSelectionModel().getSelectedItem();
        ISala salaSelezionata = combobox_sala.getSelectionModel().getSelectedItem();
        LocalDateTime orarioSelezionato = getSelectedDateTime(); // Metodo che combina DatePicker e ComboBox

        if (filmSelezionato == null || salaSelezionata == null || orarioSelezionato == null) {
            showErrorAlert("Devi selezionare un film, una sala e un orario per aggiungere uno spettacolo.");
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
        //aggiungiSpettacoloTable(nuovoSpettacolo);
        showInformationAlert("Spettacolo aggiunto con successo!");
       // aggiungiSpettacoloTable(nuovoFilm);

        //filmSerializerAdapter.serialize(films, "film.ser"); // Salva le sale dopo l'aggiunta



        // Aggiungi lo spettacolo alla lista e alla ObservableList per l'aggiornamento dell'UI
//        spettacoli.add(nuovoSpettacolo);
//        spettacoliObservableList.add(nuovoSpettacolo);

        // Opzionalmente, salva la lista aggiornata di spettacoli
        spettacoloSerializerAdapter.serialize(spettacoli, "spettacoli.ser");

       // showInformationAlert("Spettacolo aggiunto con successo!");
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
            showInformationAlert("Spettacolo rimosso con successo!");

            // Opzionalmente, salva la lista aggiornata di spettacoli
            spettacoloSerializerAdapter.serialize(spettacoli, "spettacoli.ser");

        } catch (NumberFormatException e) {
            showErrorAlert("L'ID inserito non è valido.");
        } catch (SpettacoloNonTrovatoException e) {
            showErrorAlert("Spettacolo non trovato.");
        } catch (Exception e) {
            showErrorAlert("Errore imprevisto durante la rimozione dello spettacolo: " + e.getMessage());
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

    // Funzione per mostrare un alert di errore
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Funzione per mostrare un alert di informazione
    private void showInformationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informazione");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
