package org.example.interfaccia_grafica.sale.service;

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
import exception.film.*;
import exception.sala.*;
import exception.spettacolo.SovrapposizioneSpettacoloException;
import exception.spettacolo.SpettacoloNonTrovatoException;
import org.example.interfaccia_grafica.general_utility_classes.serializzazione.ISalaDataSerializer;
import ID_persistente.IGeneratoreIDPersistente;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class GestioneSaleService implements IGestioneSaleService {
    private List<ISala> sale;
    private IGeneratoreIDPersistente generatoreID;
    //private IDataSerializer salaSerializer;
    private ISalaDataSerializer salaSerializer;
    private Amministratore amministratore;

    public GestioneSaleService(List<ISala> sale, IGeneratoreIDPersistente generatoreID, ISalaDataSerializer salaSerializer, Amministratore amministratore) {
        this.sale = sale;
        this.generatoreID = generatoreID;
        this.salaSerializer = salaSerializer;
        this.amministratore = amministratore;
    }

    public void aggiungiSala(int numeroSala, int capacita, Consumer<ISala> onSuccess) throws NumeroSalaNegativoException, NumeroPostiNegativoException, SalaGiaEsistenteException, FilmGiaPresenteException, DurataFilmNonValidaException, SpettacoloNonTrovatoException, TitoloVuotoException, SalaNonTrovataException, FilmNonValidoException, SovrapposizioneSpettacoloException, SalaNonValidaException, FilmNonTrovatoException, IOException {
        if (numeroSala <= 0) {
            throw new NumeroSalaNegativoException("Il numero della sala deve essere maggiore di zero.");
        }
        if (capacita <= 0) {
            throw new NumeroPostiNegativoException("La capacità della sala deve essere maggiore di zero.");
        }

        boolean salaEsistente = sale.stream().anyMatch(s -> s.getNumeroSala() == numeroSala);
        if (salaEsistente) {
            throw new SalaGiaEsistenteException("Una sala con questo numero esiste già.");
        }

        //    ISala nuovaSala = new Sala(numeroSala, capacita);
        IAggiungiSala servizioAggiungiSala = new AggiungiSala(sale, generatoreID);

        ISala nuovaSala = new Sala(numeroSala, capacita);
        ICommand aggiungiSalaCommand = new AggiungiSalaCommand(servizioAggiungiSala, nuovaSala);


        amministratore.setCommand(aggiungiSalaCommand);
        amministratore.eseguiComando();

        onSuccess.accept(nuovaSala);
        //salaSerializer.serialize(sale, "sale.ser");
        salaSerializer.salvaSala(sale);
    }


    public void rimuoviSala(long idSala, Consumer<Long> onSuccess) throws SalaNonTrovataException, Exception {

        try {
            IRimuoviSala servizioRimuoviSala = new RimuoviSala(sale);
            ICommand rimuoviSalaCommand = new RimuoviSalaCommand(servizioRimuoviSala, idSala);

            Amministratore amministratore = new Amministratore("Nome", "Cognome", Ruolo.AMMINISTRATORE);
            amministratore.setCommand(rimuoviSalaCommand);
            amministratore.eseguiComando();

            //salaSerializer.serialize(sale, "sale.ser"); // Salva le sale dopo la rimozione
            salaSerializer.salvaSala(sale);

            onSuccess.accept(idSala); // Esegue l'operazione di successo
        } catch (SalaNonTrovataException e) {
            throw e; // Rilancia l'eccezione specifica per gestirla al livello superiore
        } catch (Exception e) {
            throw new Exception("Errore imprevisto durante la rimozione della sala.", e);
        }
    }
}
