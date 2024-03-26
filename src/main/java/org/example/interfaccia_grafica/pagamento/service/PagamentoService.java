package org.example.interfaccia_grafica.pagamento.service;

import Serializzazione.adapter.adaptee.RegistroBigliettiSerializer;
import Serializzazione.adapter.adaptee.SalaSerializer;
import Serializzazione.adapter.adaptee.SpettacoloSerializer;
import Serializzazione.adapter.adapter.RegistroBigliettiSerializerAdapter;
import Serializzazione.adapter.adapter.SalaSerializerAdapter;
import Serializzazione.adapter.adapter.SpettacoloSerializerAdapter;
import Serializzazione.adapter.target.IDataSerializer;
import cinema_Infrastructure.sala.ISala;
import cinema_Infrastructure.spettacolo.ISpettacolo;
import domain.Utente;
import org.example.interfaccia_grafica.general_utility_classes.AlertUtil;
import org.example.interfaccia_grafica.general_utility_classes.serializzazione.ISalaDataSerializer;
import org.example.interfaccia_grafica.general_utility_classes.serializzazione.ISpettacoloDataSerializer;
import org.example.interfaccia_grafica.general_utility_classes.serializzazione.SalaDataSerializer;
import org.example.interfaccia_grafica.general_utility_classes.serializzazione.SpettacoloDataSerializer;
import payment_strategy.IMetodoPagamentoStrategy;
import payment_strategy.PayContext;
import revenues_observer.concrete_observable.RegistroBiglietti;
import revenues_observer.observable.AbstractRegistroBiglietti;
import ticket.factory.product.IBiglietto;
import user_commands.acquisto_biglietto.AcquistoBigliettoCommand;
import user_commands.annulla_biglietto.AnnullaBigliettoCommand;
import user_interfaces.AcquistoBiglietto;
import user_interfaces.IUserCommand;
import user_services.ServizioAcquistoBiglietto;

import java.io.IOException;
import java.util.List;

public class PagamentoService implements IPagamentoService {
    // Inietta le dipendenze necessarie, come il registro dei biglietti e i serializzatori
    private AbstractRegistroBiglietti registroBiglietti;
//    private IDataSerializer salaSerializer;
   // private IDataSerializer spettacoloSerializer;
    private List<ISpettacolo> spettacoli;
    private List<ISala> sale;
    private Utente utente;
    private ISalaDataSerializer salaSerializer;
    private ISpettacoloDataSerializer spettacoloSerializer;

    // Costruttore per iniettare le dipendenze
    public PagamentoService(AbstractRegistroBiglietti registroBiglietti, List<ISpettacolo> spettacoli, List<ISala> sale) {

        this.registroBiglietti = registroBiglietti;
        this.salaSerializer = new SalaDataSerializer (new SalaSerializerAdapter(new SalaSerializer()));
        this.spettacoloSerializer = new SpettacoloDataSerializer(new SpettacoloSerializerAdapter(new SpettacoloSerializer()));
        this.spettacoli = spettacoli;
        this.sale = sale;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    @Override
    public boolean eseguiPagamento(List<IBiglietto> bigliettiDaAcquistare, IMetodoPagamentoStrategy metodoPagamento) throws IOException, ClassNotFoundException {
        PayContext pagamento = new PayContext(metodoPagamento);
        AcquistoBiglietto servizioAcquisto = new ServizioAcquistoBiglietto(pagamento, registroBiglietti);

        for (IBiglietto biglietto : bigliettiDaAcquistare) {
            if (aggiornaStatoPostoAcquistato(biglietto.getSpettacolo().getId())) {
                IUserCommand acquistoCommand = new AcquistoBigliettoCommand(servizioAcquisto, biglietto);
                utente.setCommand(acquistoCommand);
                utente.eseguiComando(); // Esegui il comando senza controllare il risultato

                //salaSerializer.serialize(sale,"sale.ser");
                salaSerializer.salvaSala(sale);
                spettacoloSerializer.salvaSpettacolo(spettacoli);

                aggiornaRicaviConNuovoBiglietto(biglietto);
            } else {
                AlertUtil.showInformationAlert("Non è possibile procedere all'acquisto: posti esauriti per lo spettacolo " + biglietto.getSpettacolo().getFilm().getTitolo());
                return false; // Termina la funzione e indica che l'acquisto non è riuscito
            }
        }

        // Se il ciclo termina senza entrare nell'else, significa che tutti gli acquisti sono andati a buon fine
        return true;
    }

    private void aggiornaRicaviConNuovoBiglietto(IBiglietto nuovoBiglietto) throws IOException, ClassNotFoundException {
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

    private boolean aggiornaStatoPostoAcquistato(long idSpettacolo) throws IOException {
        // Cerca lo spettacolo tramite ID
        ISpettacolo spettacoloTrovato = spettacoli.stream()
                .filter(s -> s.getId() == idSpettacolo)
                .findFirst()
                .orElse(null);

        if (spettacoloTrovato != null && spettacoloTrovato.getSala().occupaPosto()) {
            // Se il posto è stato occupato con successo, serializza lo stato aggiornato
            salaSerializer.salvaSala(sale);
            spettacoloSerializer.salvaSpettacolo(spettacoli);
            return true; // Posto occupato e stato serializzato con successo
        } else {
            AlertUtil.showErrorAlert("La sala è piena o lo spettacolo non è stato trovato.");
            return false; // Posti esauriti o spettacolo non trovato
        }
    }

    public void annullaUltimoAcquisto(List<IBiglietto> bigliettiDaAcquistare, Runnable onAnnullamentoSuccess) throws IOException, ClassNotFoundException {
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
            onAnnullamentoSuccess.run(); // Chiama il callback
            // Libera il posto associato all'ultimo biglietto annullato
            if (liberaPostoSpettacolo(ultimoBiglietto.getSpettacolo().getId())) {
                System.out.println("Posto liberato con successo.");
            } else {
                System.out.println("Errore nella liberazione del posto.");
            }
            // Assumendo che l'annullamento sia andato a buon fine, aggiorna il registro
            // e serializza il registro aggiornato
            adapter.serialize(registroEsistente, filePath);

            // Rimuovi l'ultimo biglietto dalla lista dei biglietti acquistati
            bigliettiDaAcquistare.remove(ultimoBiglietto);

            // Mostra un Alert per confermare l'annullamento dell'acquisto
            AlertUtil.showAlert("Annullamento Confermato", "L'ultimo acquisto è stato annullato con successo.");
        } else {
            // Mostra un Alert per l'annullamento fallito
            AlertUtil.showErrorAlert("Annullamento Fallito, Non è stato possibile annullare l'acquisto.");
        }
    }

    private boolean liberaPostoSpettacolo(long idSpettacolo) throws IOException {
        // Cerca lo spettacolo tramite ID
        ISpettacolo spettacoloTrovato = spettacoli.stream()
                .filter(s -> s.getId() == idSpettacolo)
                .findFirst()
                .orElse(null);

        if (spettacoloTrovato != null) {
            // Libera il posto nello spettacolo trovato
            spettacoloTrovato.getSala().liberaPosto();
            // Serializza lo stato aggiornato della sala e degli spettacoli
            salaSerializer.salvaSala(sale);
            spettacoloSerializer.salvaSpettacolo(spettacoli);
            return true; // Posto liberato e stato serializzato con successo
        } else {
            AlertUtil.showAlert("Spettacolo non trovato.", "Lo spettacolo non è stato trovato");
            return false; // Spettacolo non trovato
        }
    }
}