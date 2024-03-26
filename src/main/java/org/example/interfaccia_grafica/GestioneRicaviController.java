package org.example.interfaccia_grafica;

import admin_commands.revenues.GeneraReportRicaviCommand;
import admin_interfaces.ICommand;
import domain.Amministratore;
import domain.Ruolo;
import exception.film.*;
import exception.sala.SalaGiaEsistenteException;
import exception.sala.SalaNonTrovataException;
import exception.sala.SalaNonValidaException;
import exception.spettacolo.SovrapposizioneSpettacoloException;
import exception.spettacolo.SpettacoloNonTrovatoException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.interfaccia_grafica.ricavi.DatiSala;
import org.example.interfaccia_grafica.service_gestionericavicontroller.GestioneRicaviService;
import org.example.interfaccia_grafica.service_gestionericavicontroller.IGestioneRicaviService;
import revenues_observer.concrete_observable.RegistroBiglietti;
import revenues_observer.concrete_observableA.AffluenzaPerSalaReport;
import revenues_observer.concrete_observableB.RicaviPerSalaReport;
import revenues_observer.observable.AbstractRegistroBiglietti;
import revenues_observer.observer.IReport;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


//public class GestioneRicaviController {
//    @FXML
//    private TableColumn<DatiSala, Number> affluenzaSalaCol_tableview;
//
//    @FXML
//    private Button generaRepRicavi_btn;
//
//    @FXML
//    private TableColumn<DatiSala, Number> ricaviRicavoCol_tableview;
//
//    @FXML
//    private TableColumn<DatiSala, String> ricaviSalaCol_tableview;
//
//    @FXML
//    private TableView<DatiSala> ricavi_tableview;
//
//    @FXML
//    private IGestioneRicaviService gestioneRicaviService;
//
//
//    @FXML
//    private void initialize() throws IOException, ClassNotFoundException {
//
//        gestioneRicaviService = new GestioneRicaviService();
//
//        ricaviSalaCol_tableview.setCellValueFactory(new PropertyValueFactory<>("nomeSala"));
//        affluenzaSalaCol_tableview.setCellValueFactory(new PropertyValueFactory<>("affluenza"));
//        ricaviRicavoCol_tableview.setCellValueFactory(new PropertyValueFactory<>("ricavi"));
//    }
//
//
//
//    @FXML
//    private void generaReportRicavi() {
//        List<DatiSala> dati = gestioneRicaviService.calcolaDatiPerSala();
//        ricavi_tableview.setItems(FXCollections.observableArrayList(dati));
//    }
//}

public class GestioneRicaviController {

    @FXML
    private TableColumn<DatiSala, Number> affluenzaSalaCol_tableview;
    @FXML
    private Button generaRepRicavi_btn;
    @FXML
    private TableColumn<DatiSala, Number> ricaviRicavoCol_tableview;
    @FXML
    private TableColumn<DatiSala, String> ricaviSalaCol_tableview;
    @FXML
    private TableView<DatiSala> ricavi_tableview;

    private Amministratore amministratore;
    private AbstractRegistroBiglietti registroBiglietti;
    private RicaviPerSalaReport ricaviReport;
    private AffluenzaPerSalaReport affluenzaReport;

    public GestioneRicaviController() {
        amministratore = new Amministratore("Luca", "Rossi", Ruolo.AMMINISTRATORE);
    }

    @FXML
    private void initialize() {
        try {
            caricaRegistroBiglietti();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Errore nel caricamento del registro dei biglietti.");
            // Qui potresti decidere di inizializzare registroBiglietti con un nuovo oggetto
            // se non riesci a caricarlo dal file
            registroBiglietti = new RegistroBiglietti(); // Fallback se il file non esiste o c'è un errore
        }

        ricaviReport = new RicaviPerSalaReport(registroBiglietti);
        affluenzaReport = new AffluenzaPerSalaReport(registroBiglietti);

        ricaviSalaCol_tableview.setCellValueFactory(new PropertyValueFactory<>("nomeSala"));
        affluenzaSalaCol_tableview.setCellValueFactory(new PropertyValueFactory<>("affluenza"));
        ricaviRicavoCol_tableview.setCellValueFactory(new PropertyValueFactory<>("ricavi"));
    }

    private void caricaRegistroBiglietti() throws IOException, ClassNotFoundException {
        String filePath = "registroBiglietti.ser";
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            registroBiglietti = (AbstractRegistroBiglietti) ois.readObject();
        }

        if (registroBiglietti == null) {
            System.out.println("Errore nel caricamento del registro dei biglietti.");
            // Inizializzazione di fallback non necessaria qui, gestita in initialize()
        }
    }

    @FXML
    private void onGeneraReportRicaviClicked() throws FilmGiaPresenteException, DurataFilmNonValidaException, SpettacoloNonTrovatoException, TitoloVuotoException, SalaGiaEsistenteException, SalaNonTrovataException, FilmNonValidoException, SovrapposizioneSpettacoloException, SalaNonValidaException, IOException, FilmNonTrovatoException {
        // Esecuzione dei comandi per la generazione dei report
        ICommand comandoRicavi = new GeneraReportRicaviCommand(ricaviReport);
        ICommand comandoAffluenza = new GeneraReportRicaviCommand(affluenzaReport);

        amministratore.setCommand(comandoRicavi);
        amministratore.eseguiComando();
        amministratore.setCommand(comandoAffluenza);
        amministratore.eseguiComando();

        // Aggiornamento dell'UI con i risultati dei report generati
        aggiornaUIDatiReport();
    }

    private void aggiornaUIDatiReport() {
        // Questo metodo dovrebbe aggregare i dati da entrambi i report e aggiornare l'UI
        // Assumendo che i report espongano metodi per ottenere i loro dati
        // La seguente implementazione è un esempio di come potrebbe essere realizzata
        Map<Integer, Double> ricaviPerSala = ricaviReport.getRicaviPerSala(); // Metodo ipotetico
        Map<Integer, Integer> affluenzaPerSala = affluenzaReport.getAffluenzaPerSala(); // Metodo ipotetico

        List<DatiSala> dati = new ArrayList<>();
        ricaviPerSala.forEach((numeroSala, ricavi) -> {
            Integer affluenza = affluenzaPerSala.getOrDefault(numeroSala, 0);
            String nomeSala = "Sala " + numeroSala;
            dati.add(new DatiSala(nomeSala, affluenza, ricavi));
        });

        ricavi_tableview.setItems(FXCollections.observableArrayList(dati));
    }
}