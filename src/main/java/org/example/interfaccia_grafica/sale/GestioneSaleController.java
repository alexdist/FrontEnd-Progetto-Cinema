package org.example.interfaccia_grafica.sale;

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
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.interfaccia_grafica.spettacoli.utility_classes.AlertUtil;
import prova_id_PERSISTENTE.GeneratoreIDPersistenteSala;
import prova_id_PERSISTENTE.IGeneratoreIDPersistente;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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

    private IGestioneSaleService gestioneSaleService;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        IDataSerializer salaSerializerAdapter = new SalaSerializerAdapter(new SalaSerializer());
        // Tentativo di caricare le sale esistenti
        try {
            sale = (List<ISala>) salaSerializerAdapter.deserialize("sale.ser");
            saleObservableList.addAll(sale);
        } catch (Exception e) {
            System.out.println("Impossibile caricare le sale esistenti. " + e.getMessage());
            sale = new ArrayList<>();
        }

        // Qui assumiamo che 'sale' sia la lista delle sale disponibili e sia già stata inizializzata

        IGeneratoreIDPersistente generatoreIDSala = new GeneratoreIDPersistenteSala();
        Amministratore amministratore = new Amministratore("Mario","Rossi",Ruolo.AMMINISTRATORE);
        this.gestioneSaleService = new GestioneSaleService(sale, generatoreIDSala, salaSerializerAdapter, amministratore);

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

            gestioneSaleService.aggiungiSala(numeroSala, capacita, nuovaSala -> {
                AlertUtil.showInformationAlert("Sala aggiunta con successo!");
                aggiungiSalaTable(nuovaSala);
            });
        } catch (NumeroSalaNegativoException | NumeroPostiNegativoException e) {
            AlertUtil.showErrorAlert("Il numero della sala e la capacità devono essere positivi.");
        } catch (SalaGiaEsistenteException e) {
            AlertUtil.showErrorAlert("La sala esiste già.");
        } catch (NumberFormatException e) {
            AlertUtil.showErrorAlert("Numero della sala o capacità non validi.");
        } catch (Exception e) {
            AlertUtil.showErrorAlert("Errore imprevisto durante l'aggiunta della sala: " + e.getMessage());
        }
    }


    @FXML
    public void rimuoviSala() {
        try {
            long idSala = Long.parseLong(IDRimuoviSala_textfiel.getText());

            Consumer<Long> onSuccess = idSalaRimosso -> {
                AlertUtil.showInformationAlert("Sala rimossa con successo!");
                rimuoviSalaTable(idSalaRimosso);
            };
            gestioneSaleService.rimuoviSala(idSala, onSuccess);

        } catch (NumberFormatException e) {
            AlertUtil.showErrorAlert("ID della sala non valido.");
        } catch (SalaNonTrovataException e) {
            AlertUtil.showErrorAlert("Sala non trovata.");
        } catch (Exception e) {
            AlertUtil.showErrorAlert(e.getMessage());
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


//    // Funzione per mostrare un alert di errore
//    private void showErrorAlert(String message) {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle("Errore");
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//
//    // Funzione per mostrare un alert di informazione
//    private void showInformationAlert(String message) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Informazione");
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }

}