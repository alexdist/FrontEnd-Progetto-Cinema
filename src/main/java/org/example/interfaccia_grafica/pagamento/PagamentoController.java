package org.example.interfaccia_grafica.pagamento;

import Serializzazione.adapter.adaptee.SalaSerializer;
import Serializzazione.adapter.adaptee.SpettacoloSerializer;
import Serializzazione.adapter.adapter.SalaSerializerAdapter;
import Serializzazione.adapter.adapter.SpettacoloSerializerAdapter;
import Serializzazione.adapter.target.IDataSerializer;
import cinema_Infrastructure.sala.ISala;
import cinema_Infrastructure.spettacolo.ISpettacolo;
import domain.Utente;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.example.interfaccia_grafica.general_utility_classes.serializzazione.ISalaDataSerializer;
import org.example.interfaccia_grafica.general_utility_classes.serializzazione.ISpettacoloDataSerializer;
import org.example.interfaccia_grafica.general_utility_classes.serializzazione.SalaDataSerializer;
import org.example.interfaccia_grafica.general_utility_classes.serializzazione.SpettacoloDataSerializer;
import org.example.interfaccia_grafica.pagamento.service.IPagamentoService;
import org.example.interfaccia_grafica.pagamento.service.PagamentoService;
import payment_strategy.*;
import revenues_observer.concrete_observable.RegistroBiglietti;
import revenues_observer.observable.AbstractRegistroBiglietti;
import ticket.factory.product.IBiglietto;

import java.io.IOException;
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
    @FXML
    private ListView<IBiglietto> listaBiglietti;

    @FXML
    private TilePane tilePaneBiglietti;

    @FXML
    private List<ISpettacolo> spettacoli;

    @FXML
    private IPagamentoService pagamentoService;

    @FXML
    private Utente utente;

    @FXML
    private List<IBiglietto> bigliettiDaAcquistare;

    @FXML
    private List<ISala> sale;

    @FXML
    private AbstractRegistroBiglietti registroBiglietti;

    @FXML
    private IMetodoPagamentoStrategy metodoPagamentoContanti;

    @FXML
    private IMetodoPagamentoStrategy metodoPagamentoCartaDiCredito;

    @FXML
    private IMetodoPagamentoStrategy metodoPagamentoBancomat;

    private ISpettacoloDataSerializer spettacoloDataSerializer;
    private ISalaDataSerializer salaDataSerializer;


    @FXML
    public void initialize() {
        spettacoloDataSerializer = new SpettacoloDataSerializer(new SpettacoloSerializerAdapter(new SpettacoloSerializer()));
        salaDataSerializer = new SalaDataSerializer(new SalaSerializerAdapter(new SalaSerializer()));

        bigliettiDaAcquistare = new ArrayList<>();
        registroBiglietti = new RegistroBiglietti();

        metodoPagamentoContanti = new PagamentoContantiStrategy();
        metodoPagamentoCartaDiCredito = new PagamentoCartaDiCreditoStrategy();
        metodoPagamentoBancomat = new PagamentoBancomatStrategy();

        try {
            sale = salaDataSerializer.caricaSala();
        } catch (Exception e) {
            System.out.println("Impossibile caricare le sale esistenti. " + e.getMessage());
            sale = new ArrayList<>();
        }

        try {
            spettacoli = spettacoloDataSerializer.caricaSpettacoli();
        } catch (Exception e) {
            System.out.println("Impossibile caricare gli spettacoli esistenti. " + e.getMessage());
            spettacoli = new ArrayList<>();
        }
        pagamentoService = new PagamentoService(registroBiglietti, spettacoli, sale);
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
        // imposta l'utente anche nel PagamentoService
        this.pagamentoService.setUtente(utente);
    }

    public void setBigliettiDaAcquistare(List<IBiglietto> biglietti) {
        this.bigliettiDaAcquistare = biglietti;
    }


    @FXML
    private void pagaInContanti() throws IOException, ClassNotFoundException {

        if (pagamentoService.eseguiPagamento(bigliettiDaAcquistare, metodoPagamentoContanti)) {
            aggiungiBigliettiAlTilePane(bigliettiDaAcquistare);
        }
    }

    @FXML
    private void pagaConCarta() throws IOException, ClassNotFoundException {

        if (pagamentoService.eseguiPagamento(bigliettiDaAcquistare, metodoPagamentoCartaDiCredito)) {
            aggiungiBigliettiAlTilePane(bigliettiDaAcquistare);
        }
    }

    @FXML
    private void pagaConBancomat() throws IOException, ClassNotFoundException {

        if (pagamentoService.eseguiPagamento(bigliettiDaAcquistare, metodoPagamentoBancomat)) {
            aggiungiBigliettiAlTilePane(bigliettiDaAcquistare);
        }
    }


    public void annullaUltimoAcquisto() throws IOException, ClassNotFoundException {
        pagamentoService.annullaUltimoAcquisto(bigliettiDaAcquistare, this::rimuoviUltimoBigliettoDalTilePane);
    }

    private void rimuoviUltimoBigliettoDalTilePane() {
        Platform.runLater(() -> {
            if (!tilePaneBiglietti.getChildren().isEmpty()) {
                tilePaneBiglietti.getChildren().remove(tilePaneBiglietti.getChildren().size() - 1);
            }
        });
    }
}







