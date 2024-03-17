package org.example.interfaccia_grafica.spettacoli.service;

import Serializzazione.adapter.target.IDataSerializer;
import admin_commands.spettacolo.aggiungi_spettacolo.AggiungiSpettacoloCommand;
import admin_commands.spettacolo.modifica_spettacolo.modifica_film.ModificaFilmPerIdSpettacoloCommand;
import admin_commands.spettacolo.modifica_spettacolo.modifica_orario.ModificaOrarioPerIdSpettacoloCommand;
import admin_commands.spettacolo.modifica_spettacolo.modifica_sala.ModificaSalaPerIdSpettacoloCommand;
import admin_commands.spettacolo.rimuovi_spettacolo.RimuoviSpettacoloCommand;
import admin_interfaces.ICommand;
import cinema_Infrastructure.film.IFilm;
import cinema_Infrastructure.sala.ISala;
import cinema_Infrastructure.spettacolo.ISpettacolo;
import cinema_Infrastructure.spettacolo.Spettacolo;
import cinema_Infrastructure.spettacolo.gestione_spettacolo.*;
import domain.Amministratore;
import exception.spettacolo.SpettacoloNonTrovatoException;
import prova_id_PERSISTENTE.IGeneratoreIDPersistente;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

public class GestioneSpettacoliService implements IGestioneSpettacoliService{

    private List<ISpettacolo> spettacoli;
    private IGeneratoreIDPersistente generatoreID;
    private IDataSerializer spettacoloSerializerAdapter;
    private Amministratore amministratore;

    public GestioneSpettacoliService(List<ISpettacolo> spettacoli, IGeneratoreIDPersistente generatoreID, IDataSerializer spettacoloSerializerAdapter, Amministratore amministratore) {
        this.spettacoli = spettacoli;
        this.generatoreID = generatoreID;
        this.spettacoloSerializerAdapter = spettacoloSerializerAdapter;
        this.amministratore = amministratore;
    }

    public void aggiungiSpettacolo(IFilm filmSelezionato, ISala salaSelezionata, LocalDateTime orarioSelezionato, Consumer<ISpettacolo> onSuccess) throws Exception {
        IAggiungiSpettacolo servizioAggiungiSpettacolo = new AggiungiSpettacolo(spettacoli, generatoreID);
        ISpettacolo nuovoSpettacolo = new Spettacolo(filmSelezionato, salaSelezionata, orarioSelezionato);

        ICommand aggiungiSpettacoloCommand = new AggiungiSpettacoloCommand(servizioAggiungiSpettacolo, nuovoSpettacolo);
        amministratore.setCommand(aggiungiSpettacoloCommand);
        amministratore.eseguiComando();
        //aggiungiSpettacoloTable(nuovoSpettacolo);
        onSuccess.accept(nuovoSpettacolo);

        // Eventuale logica di post-esecuzione, come la serializzazione
        spettacoloSerializerAdapter.serialize(spettacoli, "spettacoli.ser");
    }


    public void rimuoviSpettacolo(long idSpettacolo, Consumer<Long> onSuccess) throws Exception {
        IRimuoviSpettacolo servizioRimuoviSpettacolo = new RimuoviSpettacolo(spettacoli);
        ICommand rimuoviSpettacoloCommand = new RimuoviSpettacoloCommand(servizioRimuoviSpettacolo, idSpettacolo);

        amministratore.setCommand(rimuoviSpettacoloCommand);
        amministratore.eseguiComando();

        onSuccess.accept(idSpettacolo); // Notifica il successo dell'operazione

        spettacoloSerializerAdapter.serialize(spettacoli, "spettacoli.ser"); // Salva la lista aggiornata di spettacoli
    }

    public void modificaOrarioSpettacolo(long idSpettacolo, LocalDateTime nuovoOrario, Runnable onSuccess) throws Exception {
//        // Trova lo spettacolo da modificare basato sull'ID fornito
//        ISpettacolo spettacoloDaModificare = spettacoli.stream()
//                .filter(spettacolo -> spettacolo.getId() == idSpettacolo)
//                .findFirst()
//                .orElseThrow(() -> new SpettacoloNonTrovatoException("Spettacolo non trovato."));

        // Assumi che ModificaSpettacolo implementi IModificaSpettacolo e richieda una lista di spettacoli come dipendenza
        IModificaSpettacolo servizioModificaOrarioSpettacolo = new ModificaSpettacolo(spettacoli);

        // Crea il comando con tutti i parametri richiesti
        ICommand modificaOrarioSpettacoloCommand = new ModificaOrarioPerIdSpettacoloCommand(servizioModificaOrarioSpettacolo, idSpettacolo, nuovoOrario);

        amministratore.setCommand(modificaOrarioSpettacoloCommand);
        amministratore.eseguiComando();

        onSuccess.run(); // Esegui l'azione di successo, per esempio l'aggiornamento dell'UI

        spettacoloSerializerAdapter.serialize(spettacoli, "spettacoli.ser"); // Salva le modifiche
    }

    public void modificaSalaSpettacolo(long idSpettacolo, ISala nuovaSala, Runnable onSuccess) throws Exception {
//        ISpettacolo spettacoloDaModificare = spettacoli.stream()
//                .filter(spettacolo -> spettacolo.getId() == idSpettacolo)
//                .findFirst()
//                .orElseThrow(() -> new SpettacoloNonTrovatoException("Spettacolo non trovato."));

        IModificaSpettacolo servizioModificaSpettacolo = new ModificaSpettacolo(spettacoli);
        ICommand modificaSalaSpettacoloCommand = new ModificaSalaPerIdSpettacoloCommand(servizioModificaSpettacolo, idSpettacolo, nuovaSala);

        amministratore.setCommand(modificaSalaSpettacoloCommand);
        amministratore.eseguiComando();

        onSuccess.run(); // Esegui l'azione di successo

        spettacoloSerializerAdapter.serialize(spettacoli, "spettacoli.ser"); // Salva le modifiche
    }

    public void modificaFilmSpettacolo(long idSpettacolo, IFilm nuovoFilm, Runnable onSuccess) throws Exception {
//        ISpettacolo spettacoloDaModificare = spettacoli.stream()
//                .filter(spettacolo -> spettacolo.getId() == idSpettacolo)
//                .findFirst()
//                .orElseThrow(() -> new SpettacoloNonTrovatoException("Spettacolo non trovato."));

        IModificaSpettacolo servizioModificaSpettacolo = new ModificaSpettacolo(spettacoli);
        ICommand modificaFilmSpettacoloCommand = new ModificaFilmPerIdSpettacoloCommand(servizioModificaSpettacolo, idSpettacolo, nuovoFilm);

        amministratore.setCommand(modificaFilmSpettacoloCommand);
        amministratore.eseguiComando();

        onSuccess.run(); // Esegui l'azione di successo

        spettacoloSerializerAdapter.serialize(spettacoli, "spettacoli.ser"); // Salva le modifiche
    }
}
