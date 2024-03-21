package org.example.interfaccia_grafica;

import Serializzazione.adapter.adaptee.RegistroBigliettiSerializer;
import Serializzazione.adapter.adapter.RegistroBigliettiSerializerAdapter;
import Serializzazione.adapter.target.IDataSerializer;
import cinema_Infrastructure.spettacolo.ISpettacolo;
import domain.Utente;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.example.interfaccia_grafica.general_utility_classes.AlertUtil;
import payment_strategy.*;
import revenues_observer.concrete_observable.RegistroBiglietti;
import revenues_observer.concrete_observableA.AffluenzaPerSalaReport;
import revenues_observer.concrete_observableB.RicaviPerSalaReport;
import revenues_observer.observable.AbstractRegistroBiglietti;
import ticket.factory.product.IBiglietto;
import user_commands.acquisto_biglietto.AcquistoBigliettoCommand;
import user_commands.annulla_biglietto.AnnullaBigliettoCommand;
import user_interfaces.AcquistoBiglietto;
import user_interfaces.IUserCommand;
import user_services.ServizioAcquistoBiglietto;

import java.util.ArrayList;
import java.util.List;

public class PagamentoController {

    @FXML
    private Button annulla_button;

    @FXML
    private Button pagaConBancomat_button;

    @FXML
    private Button pagaConCarta_button;

    @FXML
    private Button pagaInContanti_button;

    private Utente utente;

    // Assumi di avere un elenco di biglietti da acquistare
    private List<IBiglietto> bigliettiDaAcquistare = new ArrayList<>();

    private AbstractRegistroBiglietti registroBiglietti = new RegistroBiglietti();

    @FXML
    private ListView<IBiglietto> listaBiglietti;

    @FXML
    private TilePane tilePaneBiglietti;






    @FXML
    public void initialize() {

    }



    public void aggiungiBigliettiAlTilePane(List<IBiglietto> biglietti) {
        for (IBiglietto biglietto : biglietti) {
            VBox pannelloBiglietto = new VBox(10);
            pannelloBiglietto.setPadding(new Insets(10));
            pannelloBiglietto.setBackground(new Background(new BackgroundFill(Color.web(biglietto.getColore()), CornerRadii.EMPTY, Insets.EMPTY)));

            // Crea Label con testo bianco
            Label nomeAcquirente = new Label(biglietto.getAcquirente().getNome() + " " + biglietto.getAcquirente().getCognome());
            Label costo = new Label(String.format("Costo: %.2f €", biglietto.getCosto()));
            Label film = new Label("Film: " + biglietto.getSpettacolo().getFilm().getTitolo());
            Label sala = new Label("Sala: " + biglietto.getSpettacolo().getSala().getNumeroSala());
            Label orario = new Label("Ora: " + biglietto.getSpettacolo().getOrarioProiezione().toString());

            // Imposta lo stile dei Label per avere il testo bianco
            String labelStyle = "-fx-text-fill: white;";
            nomeAcquirente.setStyle(labelStyle);
            costo.setStyle(labelStyle);
            film.setStyle(labelStyle);
            sala.setStyle(labelStyle);
            orario.setStyle(labelStyle);

            // Aggiunge tutti i Label al VBox
            pannelloBiglietto.getChildren().addAll(nomeAcquirente, costo, film, sala, orario);

            // Aggiunge il VBox al TilePane
            tilePaneBiglietti.getChildren().add(pannelloBiglietto);
        }
    }




    public void setUtente(Utente utente) {
        this.utente = utente;
        // Aggiorna la vista o effettua operazioni basate sull'utente qui, se necessario
    }

    public void setBigliettiDaAcquistare(List<IBiglietto> biglietti) {
        this.bigliettiDaAcquistare = biglietti;
        // Aggiorna la ListView con i nuovi biglietti
        //listaBiglietti.setItems(FXCollections.observableArrayList(bigliettiDaAcquistare));
        //aggiungiBigliettiAlTilePane(biglietti);
    }



    @FXML
    private void pagaInContanti() {
        IMetodoPagamentoStrategy metodoPagamento = new PagamentoContantiStrategy();
        eseguiPagamento(metodoPagamento);
        aggiungiBigliettiAlTilePane(bigliettiDaAcquistare);
    }

    @FXML
    private void pagaConCarta() {
        IMetodoPagamentoStrategy metodoPagamento = new PagamentoCartaDiCreditoStrategy();
        eseguiPagamento(metodoPagamento);
        aggiungiBigliettiAlTilePane(bigliettiDaAcquistare);
    }

    @FXML
    private void pagaConBancomat() {
        IMetodoPagamentoStrategy metodoPagamento = new PagamentoBancomatStrategy();
        eseguiPagamento(metodoPagamento);
        aggiungiBigliettiAlTilePane(bigliettiDaAcquistare);
    }

    private void eseguiPagamento(IMetodoPagamentoStrategy metodoPagamento) {
        PayContext pagamento = new PayContext(metodoPagamento);



        AcquistoBiglietto servizioAcquisto = new ServizioAcquistoBiglietto(pagamento, registroBiglietti);

        for (IBiglietto biglietto : bigliettiDaAcquistare) {
            IUserCommand acquistoCommand = new AcquistoBigliettoCommand(servizioAcquisto, biglietto);
            utente.setCommand(acquistoCommand);
            utente.eseguiComando();

            aggiornaRicaviConNuovoBiglietto(biglietto);
        }
        //visualizzaBigliettiAcquistati();
        mostraBigliettiAcquistatiConAlert();
        // Gestisci le azioni post-pagamento come la conferma dell'acquisto
        // Serializzazione del registro biglietti dopo l'acquisto

        //serializzaRegistroBiglietti();
    }


//    public void annullaUltimoAcquisto() {
//        if (bigliettiDaAcquistare.isEmpty()) {
//            System.out.println("Non ci sono acquisti recenti da annullare.");
//            return;
//        }
//
//        // Carica il registro dei biglietti esistente
//        IDataSerializer adapter = new RegistroBigliettiSerializerAdapter(new RegistroBigliettiSerializer());
//        String filePath = "registroBiglietti.ser";
//        AbstractRegistroBiglietti registroEsistente = (AbstractRegistroBiglietti) adapter.deserialize(filePath);
//
//        if (registroEsistente == null) {
//            System.out.println("Errore nel caricamento del registro dei biglietti.");
//            return;
//        }
//
//        // Ottiene l'ultimo biglietto acquistato
//        IBiglietto ultimoBiglietto = bigliettiDaAcquistare.get(bigliettiDaAcquistare.size() - 1);
//
//        // Crea il comando per annullare l'acquisto del biglietto
//        IUserCommand annullaCommand = new AnnullaBigliettoCommand(ultimoBiglietto.getId(), registroEsistente);
//        utente.setCommand(annullaCommand);
//        utente.eseguiComando();
//
//        // Assumendo che l'annullamento sia andato a buon fine, aggiorna il registro
//        // e serializza il registro aggiornato
//        adapter.serialize(registroEsistente, filePath);
//
//        // Rimuovi l'ultimo biglietto dalla lista dei biglietti acquistati
//        bigliettiDaAcquistare.remove(ultimoBiglietto);
//    }

//    public void annullaUltimoAcquisto() {
//        if (bigliettiDaAcquistare.isEmpty()) {
//            AlertUtil.showInformationAlert("Annullamento Acquisto. Non ci sono acquisti recenti da annullare.");
//            return;
//        }
//
//        // Carica il registro dei biglietti esistente
//        IDataSerializer adapter = new RegistroBigliettiSerializerAdapter(new RegistroBigliettiSerializer());
//        String filePath = "registroBiglietti.ser";
//        AbstractRegistroBiglietti registroEsistente = (AbstractRegistroBiglietti) adapter.deserialize(filePath);
//
//        if (registroEsistente == null) {
//            AlertUtil.showErrorAlert("Errore nel caricamento del registro dei biglietti.");
//            return;
//        }
//
//        // Ottiene l'ultimo biglietto acquistato
//        IBiglietto ultimoBiglietto = bigliettiDaAcquistare.get(bigliettiDaAcquistare.size() - 1);
//
//        // Crea il comando per annullare l'acquisto del biglietto
//        IUserCommand annullaCommand = new AnnullaBigliettoCommand(ultimoBiglietto.getId(), registroEsistente);
//        utente.setCommand(annullaCommand);
//        utente.eseguiComando();
//
//        // Assumendo che l'annullamento sia andato a buon fine, aggiorna il registro
//        // e serializza il registro aggiornato
//        adapter.serialize(registroEsistente, filePath);
//
//        // Rimuovi l'ultimo biglietto dalla lista dei biglietti acquistati
//        bigliettiDaAcquistare.remove(ultimoBiglietto);
//
//        rimuoviUltimoBigliettoDalTilePane();
//
//        // Mostra un Alert per confermare l'annullamento dell'acquisto
//        AlertUtil.showAlert("Annullamento Confermato", "L'ultimo acquisto è stato annullato con successo.");
//    }

    public void annullaUltimoAcquisto() {
        if (bigliettiDaAcquistare.isEmpty()) {
            AlertUtil.showInformationAlert("Annullamento Acquisto. Non ci sono acquisti recenti da annullare.");
            return;
        }

        // Carica il registro dei biglietti esistente
        IDataSerializer adapter = new RegistroBigliettiSerializerAdapter(new RegistroBigliettiSerializer());
        String filePath = "registroBiglietti.ser";
        AbstractRegistroBiglietti registroEsistente = (AbstractRegistroBiglietti) adapter.deserialize(filePath);

        if (registroEsistente == null) {
            AlertUtil.showErrorAlert("Errore nel caricamento del registro dei biglietti.");
            return;
        }

        // Ottiene l'ultimo biglietto acquistato
        IBiglietto ultimoBiglietto = bigliettiDaAcquistare.get(bigliettiDaAcquistare.size() - 1);

        // Crea il comando per annullare l'acquisto del biglietto
        IUserCommand annullaCommand = new AnnullaBigliettoCommand(ultimoBiglietto.getId(), registroEsistente);
        utente.setCommand(annullaCommand);
        utente.eseguiComando();

        if (registroEsistente.isUltimoAnnullamentoRiuscito()) {
            // Assumendo che l'annullamento sia andato a buon fine, aggiorna il registro
            // e serializza il registro aggiornato
            adapter.serialize(registroEsistente, filePath);

            // Rimuovi l'ultimo biglietto dalla lista dei biglietti acquistati
            bigliettiDaAcquistare.remove(ultimoBiglietto);

            // Rimuovi l'ultimo VBox corrispondente al biglietto dal TilePane
            rimuoviUltimoBigliettoDalTilePane();

            // Mostra un Alert per confermare l'annullamento dell'acquisto
            AlertUtil.showAlert("Annullamento Confermato", "L'ultimo acquisto è stato annullato con successo.");
        } else {
            // Mostra un Alert per l'annullamento fallito
            AlertUtil.showAlert("Annullamento Fallito", "Non è stato possibile annullare l'acquisto.");
        }
    }

    private void rimuoviUltimoBigliettoDalTilePane() {
        if (!tilePaneBiglietti.getChildren().isEmpty()) {
            // Rimuove l'ultimo elemento (VBox) aggiunto al TilePane
            tilePaneBiglietti.getChildren().remove(tilePaneBiglietti.getChildren().size() - 1);
        }
    }

    private void aggiornaRicaviConNuovoBiglietto(IBiglietto nuovoBiglietto) {
        // Carica il registro dei biglietti esistente
        IDataSerializer adapter = new RegistroBigliettiSerializerAdapter(new RegistroBigliettiSerializer());
        String filePath = "registroBiglietti.ser";
        AbstractRegistroBiglietti registroEsistente = (AbstractRegistroBiglietti) adapter.deserialize(filePath);

        if (registroEsistente == null) {
            registroEsistente = new RegistroBiglietti(); // O creane uno nuovo se non esiste
        }

        // Aggiungi il nuovo biglietto al registro
        registroEsistente.aggiungiBiglietto(nuovoBiglietto);

        // Serializza il registro aggiornato
        adapter.serialize(registroEsistente, filePath);
    }

    private void serializzaRegistroBiglietti() {
        // Ad esempio, usando un adapter per la serializzazione
        IDataSerializer adapter = new RegistroBigliettiSerializerAdapter(new RegistroBigliettiSerializer());
        String filePath = "registroBiglietti.ser";
        adapter.serialize(registroBiglietti, filePath);
    }

    public void visualizzaBigliettiAcquistati() {
        System.out.println("Dettagli dei biglietti acquistati:");
        for (IBiglietto biglietto : bigliettiDaAcquistare) {
            Utente acquirente = biglietto.getAcquirente();
            ISpettacolo spettacolo = biglietto.getSpettacolo();
            String dettagliBiglietto = String.format(
                    "Biglietto ID: %s\nAcquirente: %s %s\nFilm: %s\nData e Ora: %s\nPrezzo: %.2f€\n-----",
                    biglietto.getId(),
                    acquirente.getNome(),
                    acquirente.getCognome(),
                    spettacolo.getFilm().getTitolo(),
                    spettacolo.getOrarioProiezione().toString(),
                    biglietto.getCosto()
            );
            System.out.println(dettagliBiglietto);
        }
    }

    public void mostraBigliettiAcquistatiConAlert() {
        StringBuilder dettagli = new StringBuilder("Dettagli dei biglietti acquistati:\n");
        for (IBiglietto biglietto : bigliettiDaAcquistare) {
            Utente acquirente = biglietto.getAcquirente();
            ISpettacolo spettacolo = biglietto.getSpettacolo();
            dettagli.append(String.format(
                    "Biglietto ID: %s\nAcquirente: %s %s\nFilm: %s\nData e Ora: %s\nPrezzo: %.2f€\n-----\n",
                    biglietto.getId(),
                    acquirente.getNome(),
                    acquirente.getCognome(),
                    spettacolo.getFilm().getTitolo(),
                    spettacolo.getOrarioProiezione().toString(),
                    biglietto.getCosto()
            ));
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Biglietti Acquistati");
        alert.setHeaderText(null);
        alert.setContentText(dettagli.toString());
        alert.showAndWait();
    }


    }
