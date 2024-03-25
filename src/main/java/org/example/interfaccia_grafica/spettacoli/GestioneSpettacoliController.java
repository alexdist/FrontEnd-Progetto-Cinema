package org.example.interfaccia_grafica.spettacoli;

import Serializzazione.adapter.adaptee.FilmSerializer;
import Serializzazione.adapter.adaptee.SalaSerializer;
import Serializzazione.adapter.adaptee.SpettacoloSerializer;
import Serializzazione.adapter.adapter.FilmSerializerAdapter;
import Serializzazione.adapter.adapter.SalaSerializerAdapter;
import Serializzazione.adapter.adapter.SpettacoloSerializerAdapter;
import Serializzazione.adapter.target.IDataSerializer;
import cinema_Infrastructure.film.IFilm;
import cinema_Infrastructure.sala.ISala;
import cinema_Infrastructure.spettacolo.ISpettacolo;
import domain.Amministratore;
import domain.Ruolo;
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
import org.example.interfaccia_grafica.general_utility_classes.AlertUtil;
import org.example.interfaccia_grafica.spettacoli.service.GestioneSpettacoliService;
import org.example.interfaccia_grafica.spettacoli.service.IGestioneSpettacoliService;
import org.example.interfaccia_grafica.spettacoli.utility_classes.*;
import prova_id_PERSISTENTE.GeneratoreIDPersistenteSpettacolo;

import java.io.IOException;
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
    @FXML
    private ObservableList<ISpettacolo> spettacoliObservableList = FXCollections.observableArrayList();
    @FXML
    private List<ISpettacolo> spettacoli;
    @FXML
    private ObservableList<IFilm> filmObservableList = FXCollections.observableArrayList();
    @FXML
    private ObservableList<ISala> salaObservableList = FXCollections.observableArrayList();

    SpettacoloSerializer spettacoloSerializer = new SpettacoloSerializer();
    IDataSerializer spettacoloSerializerAdapter = new SpettacoloSerializerAdapter(spettacoloSerializer);

    FilmSerializer filmSerializer = new FilmSerializer();
    IDataSerializer filmSerializerAdapter = new FilmSerializerAdapter(filmSerializer);

    // Adapter instances
    SalaSerializer salaSerializer = new SalaSerializer();
    IDataSerializer salaSerializerAdapter = new SalaSerializerAdapter(salaSerializer);

    Amministratore amministratore = new Amministratore("Nome", "Cognome", Ruolo.AMMINISTRATORE);

    private IGestioneSpettacoliService gestioneSpettacoliService;


    @Override
    public void initialize(URL location, ResourceBundle resources){

        try {
            spettacoli = (List<ISpettacolo>) spettacoloSerializerAdapter.deserialize("spettacoli.ser");
            spettacoliObservableList.addAll(spettacoli);
        } catch (Exception e) {
            System.out.println("Impossibile caricare gli spettacoli esistenti. " + e.getMessage());
            spettacoli = new ArrayList<>();
        }


        gestioneSpettacoliService = new GestioneSpettacoliService(spettacoli, new GeneratoreIDPersistenteSpettacolo(), spettacoloSerializerAdapter, amministratore);


        hoursComboBox.setItems(FXCollections.observableArrayList(IntStream.rangeClosed(0, 23).boxed().collect(Collectors.toList())));

        minutesComboBox.setItems(FXCollections.observableArrayList(
                IntStream.rangeClosed(0, 59)
                        .filter(i -> i % 5 == 0)
                        .boxed()
                        .collect(Collectors.toList())
        ));

        hoursComboBox_modifica.setItems(FXCollections.observableArrayList(IntStream.rangeClosed(0, 23).boxed().collect(Collectors.toList())));

        minutesComboBox_modifica.setItems(FXCollections.observableArrayList(
                IntStream.rangeClosed(0, 59)
                        .filter(i -> i % 5 == 0)
                        .boxed()
                        .collect(Collectors.toList())
        ));

//        // Imposta un valore predefinito se necessario
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
    public void aggiungiSpettacolo() {
        try {
            IFilm filmSelezionato = combobox_film.getSelectionModel().getSelectedItem();
            ISala salaSelezionata = combobox_sala.getSelectionModel().getSelectedItem();
            LocalDateTime orarioSelezionato = DateTimeUtil.getSelectedDateTime(datePicker, hoursComboBox, minutesComboBox);

            gestioneSpettacoliService.aggiungiSpettacolo(filmSelezionato, salaSelezionata, orarioSelezionato, this::aggiungiSpettacoloTable);

            // Aggiornamento dell'UI post-aggiunta spettacolo
            AlertUtil.showInformationAlert("Spettacolo aggiunto con successo!");
        } catch (Exception e) {
            AlertUtil.showErrorAlert("Errore nell'aggiunta dello spettacolo: " + e.getMessage());
        }
    }
    @FXML
    private void modificaOrario() {
        try {
            long idSpettacolo = Long.parseLong(IDmodOrarioSpett_textfield.getText().trim());
            LocalDate data = datePicker_modifica.getValue();
            Integer ora = hoursComboBox_modifica.getSelectionModel().getSelectedItem();
            Integer minuti = minutesComboBox_modifica.getSelectionModel().getSelectedItem();

            if (data == null || ora == null || minuti == null) {
                AlertUtil.showAlert("Errore", "Devi selezionare una data e un'ora completa.");
                return;
            }

            LocalDateTime orarioModificato = LocalDateTime.of(data, LocalTime.of(ora, minuti));

            gestioneSpettacoliService.modificaOrarioSpettacolo(idSpettacolo, orarioModificato, () -> {
                spettacolo_tableview.refresh(); // Aggiorna l'UI
                AlertUtil.showAlert("Successo", "Orario dello spettacolo modificato con successo.");
            });
        } catch (NumberFormatException e) {
            AlertUtil.showAlert("Errore di formato", "L'ID inserito non è valido.");
        } catch (SpettacoloNonTrovatoException e) {
            AlertUtil.showAlert("Errore", "Spettacolo non trovato.");
        } catch (Exception e) {
            AlertUtil.showAlert("Errore", "Errore durante la modifica dell'orario dello spettacolo: " + e.getMessage());
        }
    }
    @FXML
    private void modificaSala() {
        try {
            long idSpettacolo = Long.parseLong(IDmodSalaSpett_textfield.getText().trim());
            ISala salaSelezionata = combobox_sala_modifica.getSelectionModel().getSelectedItem();

            if (salaSelezionata == null) {
                AlertUtil.showAlert("Errore", "Seleziona una sala da assegnare allo spettacolo.");
                return;
            }

            gestioneSpettacoliService.modificaSalaSpettacolo(idSpettacolo, salaSelezionata, () -> {
                spettacolo_tableview.refresh(); // Aggiorna l'UI
                AlertUtil.showAlert("Successo", "Sala dello spettacolo modificato con successo.");
            });
        } catch (NumberFormatException e) {
            AlertUtil.showAlert("Errore di formato", "L'ID inserito non è valido.");
        } catch (Exception e) {
            AlertUtil.showAlert("Errore", "Errore durante la modifica dello spettacolo: " + e.getMessage());
        }
    }
    @FXML
    private void modificaFilm() {
        try {
            long idSpettacolo = Long.parseLong(IDmodFilmSpett_textfield.getText().trim());
            IFilm filmSelezionato = combobox_film_modifica.getSelectionModel().getSelectedItem();

            if (filmSelezionato == null) {
                AlertUtil.showAlert("Errore", "Seleziona un film da assegnare allo spettacolo.");
                return;
            }

            gestioneSpettacoliService.modificaFilmSpettacolo(idSpettacolo, filmSelezionato, () -> {
                spettacolo_tableview.refresh(); // Aggiorna l'UI
                AlertUtil.showAlert("Successo", "Film dello spettacolo modificato con successo.");
            });
        } catch (NumberFormatException e) {
            AlertUtil.showAlert("Errore di formato", "L'ID inserito non è valido.");
        } catch (Exception e) {
            AlertUtil.showAlert("Errore", "Errore durante la modifica dello spettacolo: " + e.getMessage());
        }
    }

    @FXML
    public void aggiungiSpettacoloTable(ISpettacolo nuovoSpettacolo) {
        spettacoliObservableList.add(nuovoSpettacolo);
    }

    @FXML
    public void rimuoviSpettacolo() {
        try {
            long idSpettacolo = Long.parseLong(IDRimuoviSpett_textfield.getText());

            gestioneSpettacoliService.rimuoviSpettacolo(idSpettacolo, this::rimuoviSpettacoloTable);

            AlertUtil.showInformationAlert("Spettacolo rimosso con successo!");
        } catch (NumberFormatException e) {
            AlertUtil.showErrorAlert("L'ID inserito non è valido.");
        } catch (SpettacoloNonTrovatoException e) {
            AlertUtil.showErrorAlert("Spettacolo non trovato.");
        } catch (Exception e) {
            AlertUtil.showErrorAlert("Errore imprevisto durante la rimozione dello spettacolo: " + e.getMessage());
        }
    }
    @FXML
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
    @FXML
    private List<IFilm> getFilms() {
        // Questo metodo deve recuperare l'elenco dei film, ad esempio:
        try {
            return (List<IFilm>) filmSerializerAdapter.deserialize("film.ser");
        } catch (Exception e) {
            // gestire l'eccezione e ritornare una lista vuota o gestire di conseguenza
            return new ArrayList<>();
        }
    }
    @FXML
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
