package org.example.interfaccia_grafica;

import Serializzazione.adapter.adaptee.SalaSerializer;
import Serializzazione.adapter.adapter.SalaSerializerAdapter;
import Serializzazione.adapter.target.IDataSerializer;
import admin_commands.sala.aggiungi_sala.AggiungiSalaCommand;
import admin_commands.sala.rimuovi_sala.RimuoviSalaCommand;
import admin_interfaces.ICommand;
import cinema_Infrastructure.sala.ISala;
import cinema_Infrastructure.sala.Sala;
import cinema_Infrastructure.sala.gestione_sala.AggiungiSala;
import cinema_Infrastructure.sala.gestione_sala.IAggiungiSala;
import cinema_Infrastructure.sala.gestione_sala.IRimuoviSala;
import cinema_Infrastructure.sala.gestione_sala.RimuoviSala;
import domain.Amministratore;
import domain.Ruolo;
import exception.sala.*;
import id_generator_factory.abstract_factory.GeneratoreIDFactory;
import id_generator_factory.concrete_factories.GeneratoreIDSalaFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import prova_id_PERSISTENTE.GeneratoreIDPersistente;
import prova_id_PERSISTENTE.IGeneratoreIDPersistente;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GestioneSaleController implements Initializable {

    @FXML
    private TableView<ISala> sala_tableview;
    @FXML
    private TableColumn<Sala, Integer> numeroSalaCol_tableview;
    @FXML
    private TableColumn<Sala, Integer> capacitaCol_tableview;
    @FXML
    private TableColumn<Sala, Long> IDCol_tableview;



    @FXML
    private TextField IDRimuoviSala_textfiel;

    @FXML
    private Button aggiungiSala_btn;



    @FXML
    private TextField capacitaSala_textflied;

    @FXML
    private Button eliminaSala_btn;



    @FXML
    private TextField numeroSala_textfiel;

    private ObservableList<ISala> saleObservableList = FXCollections.observableArrayList();



    private List<ISala> sale;

    // Adapter instances
    SalaSerializer salaSerializer = new SalaSerializer();
    IDataSerializer salaSerializerAdapter = new SalaSerializerAdapter(salaSerializer);


    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        // Inizializzazione dell'ambiente di test
//        sale = new ArrayList<>();
//        numeroSalaCol_tableview.setCellValueFactory(new PropertyValueFactory<>("numeroSala"));
//        capacitaCol_tableview.setCellValueFactory(new PropertyValueFactory<>("capacita"));
//        IDCol_tableview.setCellValueFactory(new PropertyValueFactory<>("id"));
//
//        sala_tableview.setItems(saleObservableList);
//
//    }
    public void initialize(URL location, ResourceBundle resources) {
        // Tentativo di caricare le sale esistenti
        try {
            sale = (List<ISala>) salaSerializerAdapter.deserialize("sale.ser");
            saleObservableList.addAll(sale);
        } catch (Exception e) {
            System.out.println("Impossibile caricare le sale esistenti. " + e.getMessage());
            sale = new ArrayList<>();
        }

        numeroSalaCol_tableview.setCellValueFactory(new PropertyValueFactory<>("numeroSala"));
        capacitaCol_tableview.setCellValueFactory(new PropertyValueFactory<>("capacita"));
        IDCol_tableview.setCellValueFactory(new PropertyValueFactory<>("id"));
        sala_tableview.setItems(saleObservableList);
    }


    @FXML
    public void aggiungiSala() {
        try {
            int numeroSala = Integer.parseInt(numeroSala_textfiel.getText());
            int capacita = Integer.parseInt(capacitaSala_textflied.getText());

            // Assicurati che il numero della sala e la capacità siano positivi
            if(numeroSala <= 0) throw new NumeroSalaNegativoException();
            if(capacita <= 0) throw new NumeroPostiNegativoException();

            //GeneratoreIDFactory generatoreIDSalaFactory = new GeneratoreIDSalaFactory();
            IGeneratoreIDPersistente generatoreIDSala = new GeneratoreIDPersistente();

            IAggiungiSala servizioAggiungiSala = new AggiungiSala(sale, generatoreIDSala);

            ISala nuovaSala = new Sala(numeroSala, capacita);
            ICommand aggiungiSalaCommand = new AggiungiSalaCommand(servizioAggiungiSala, nuovaSala);

            Amministratore amministratore = new Amministratore("Nome", "Cognome", Ruolo.AMMINISTRATORE);
            amministratore.setCommand(aggiungiSalaCommand);
            amministratore.eseguiComando();
            showInformationAlert("Sala aggiunta con successo!");
            aggiungiSalaTable(nuovaSala);

            salaSerializerAdapter.serialize(sale, "sale.ser"); // Salva le sale dopo l'aggiunta
        } catch (NumeroSalaNegativoException | NumeroPostiNegativoException e) {
            showErrorAlert("Il numero della sala e la capacità devono essere positivi.");
        } catch (SalaGiaEsistenteException e) {
            showErrorAlert("La sala esiste già.");
        } catch (Exception e) {
            showErrorAlert("Errore imprevisto durante l'aggiunta della sala.");
        }
    }

    @FXML
    public void rimuoviSala() {
        try {
            long idSala = Long.parseLong(IDRimuoviSala_textfiel.getText());

            IRimuoviSala servizioRimuoviSala = new RimuoviSala(sale);
            ICommand rimuoviSalaCommand = new RimuoviSalaCommand(servizioRimuoviSala, idSala);

            Amministratore amministratore = new Amministratore("Nome", "Cognome", Ruolo.AMMINISTRATORE);
            amministratore.setCommand(rimuoviSalaCommand);
            amministratore.eseguiComando();
            showInformationAlert("Sala rimossa con successo!");
            rimuoviSalaTable(idSala);

            salaSerializerAdapter.serialize(sale, "sale.ser"); // Salva le sale dopo la rimozione
        }catch (SalaNonTrovataException e) {
            showErrorAlert("Sala non trovata.");
        } catch (Exception e) {
            showErrorAlert("Errore imprevisto durante la rimozione della sala.");
        }
    }

    public void aggiungiSalaTable(ISala nuovaSala) {
        saleObservableList.add(nuovaSala);
    }

    public void rimuoviSalaTable(long idSalaDaRimuovere) {
        // Trova la sala con l'ID specificato
        ISala salaTrovata = null;
        for (ISala sala : saleObservableList) {
            if (sala.getId() == idSalaDaRimuovere) {
                salaTrovata = sala;
                break;
            }
        }

        // Rimuovi la sala trovata dalla lista osservabile, se presente
        if (salaTrovata != null) {
            saleObservableList.remove(salaTrovata);
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
