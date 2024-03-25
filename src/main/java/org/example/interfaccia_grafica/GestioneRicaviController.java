package org.example.interfaccia_grafica;

import Serializzazione.adapter.adaptee.RegistroBigliettiSerializer;
import Serializzazione.adapter.adapter.RegistroBigliettiSerializerAdapter;
import Serializzazione.adapter.target.IDataSerializer;
import admin_commands.revenues.GeneraReportRicaviCommand;
import admin_interfaces.ICommand;
import domain.Amministratore;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.interfaccia_grafica.ricavi.DatiSala;
//import org.example.interfaccia_grafica.service_gestionericavicontroller.GestioneBigliettiService;
import org.example.interfaccia_grafica.service_gestionericavicontroller.GestioneRicaviService;
import org.example.interfaccia_grafica.service_gestionericavicontroller.IGestioneRicaviService;
import revenues_observer.concrete_observableA.AffluenzaPerSalaReport;
import revenues_observer.concrete_observableB.RicaviPerSalaReport;
import revenues_observer.observable.AbstractRegistroBiglietti;
import ticket.factory.product.IBiglietto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private AbstractRegistroBiglietti registroBiglietti;

    private IGestioneRicaviService gestioneRicaviService;

    // public void setRegistroBiglietti(AbstractRegistroBiglietti registro) {
//        this.registroBiglietti = registro;
//    }

    @FXML
    private void initialize() throws IOException, ClassNotFoundException {

        gestioneRicaviService = new GestioneRicaviService();

        //caricaRegistroBiglietti();

        ricaviSalaCol_tableview.setCellValueFactory(new PropertyValueFactory<>("nomeSala"));
        affluenzaSalaCol_tableview.setCellValueFactory(new PropertyValueFactory<>("affluenza"));
        ricaviRicavoCol_tableview.setCellValueFactory(new PropertyValueFactory<>("ricavi"));
    }


    private void caricaRegistroBiglietti() throws IOException, ClassNotFoundException {
        // Percorso del file da cui deserializzare l'oggetto
        String filePath = "registroBiglietti.ser";
        IDataSerializer adapter = new RegistroBigliettiSerializerAdapter(new RegistroBigliettiSerializer());
        registroBiglietti = (AbstractRegistroBiglietti) adapter.deserialize(filePath);

        if (registroBiglietti == null) {
            System.out.println("Errore nel caricamento del registro dei biglietti.");
            // Qui potresti decidere di inizializzare registroBiglietti con un nuovo oggetto
            // registroBiglietti = new RegistroBiglietti();
        }
    }

    @FXML
    private void generaReportRicavi() {
//        if (registroBiglietti == null) {
//            System.out.println("Registro biglietti non disponibile.");
//            return;
//        }
//
//        // Genera i report (non modificati, quindi non restituiranno direttamente i dati)
//        RicaviPerSalaReport ricaviReport = new RicaviPerSalaReport(registroBiglietti);
//        AffluenzaPerSalaReport affluenzaReport = new AffluenzaPerSalaReport(registroBiglietti);
//        ricaviReport.generate();
//        affluenzaReport.generate();
//
//        // Qui replichi la logica per calcolare affluenza e ricavi direttamente nel controller
//        Map<Integer, Integer> affluenzaPerSala = new HashMap<>();
//        Map<Integer, Double> ricaviPerSala = new HashMap<>();
//
//        // Assumi che possiamo ottenere i biglietti dal registroBiglietti (come nei report)
//        List<IBiglietto> biglietti = registroBiglietti.getBiglietti();
//
//        for (IBiglietto biglietto : biglietti) {
//            int numeroSala = biglietto.getSpettacolo().getSala().getNumeroSala();
//            affluenzaPerSala.merge(numeroSala, 1, Integer::sum);
//            ricaviPerSala.merge(numeroSala, biglietto.getCosto(), Double::sum);
//        }
//
//        // Ora hai le mappe affluenzaPerSala e ricaviPerSala popolate come nei report
//        // Puoi usare queste mappe per creare i tuoi oggetti DatiSala e aggiungerli alla lista dati
//        List<DatiSala> dati = new ArrayList<>();
//        affluenzaPerSala.forEach((numeroSala, affluenza) -> {
//            Double ricavi = ricaviPerSala.getOrDefault(numeroSala, 0.0);
//            // Qui puoi convertire il numeroSala in una stringa nomeSala se necessario
//            String nomeSala = "Sala " + numeroSala; // Esempio di conversione
//            dati.add(new DatiSala(nomeSala, affluenza, ricavi));
//        });
        List<DatiSala> dati = gestioneRicaviService.calcolaDatiPerSala();
        ricavi_tableview.setItems(FXCollections.observableArrayList(dati));
    }
}