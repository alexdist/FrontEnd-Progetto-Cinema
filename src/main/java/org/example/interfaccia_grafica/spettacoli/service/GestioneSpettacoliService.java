package org.example.interfaccia_grafica.spettacoli.service;

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
import org.example.interfaccia_grafica.general_utility_classes.serializzazione.ISpettacoloDataSerializer;
import ID_persistente.IGeneratoreIDPersistente;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

public class GestioneSpettacoliService implements IGestioneSpettacoliService{//

    private List<ISpettacolo> spettacoli;
    private IGeneratoreIDPersistente generatoreID;
    private ISpettacoloDataSerializer spettacoloDataSerializer;
    private Amministratore amministratore;

    public GestioneSpettacoliService(List<ISpettacolo> spettacoli, IGeneratoreIDPersistente generatoreID, ISpettacoloDataSerializer spettacoloDataSerializer, Amministratore amministratore) {
        this.spettacoli = spettacoli;
        this.generatoreID = generatoreID;
        this.spettacoloDataSerializer = spettacoloDataSerializer;
        this.amministratore = amministratore;
    }

    public void aggiungiSpettacolo(IFilm filmSelezionato, ISala salaSelezionata, LocalDateTime orarioSelezionato, Consumer<ISpettacolo> onSuccess) throws Exception {
        IAggiungiSpettacolo servizioAggiungiSpettacolo = new AggiungiSpettacolo(spettacoli, generatoreID);

        // Qui uso clone() per assicurare che ogni spettacolo abbia una propria istanza di sala, poiché ho avuto problemi quando decrementavo il numero di posti a sedere per uno spettacolo. Gli spettacoli che avevano lo stesso numero di sala sortivano lo stesso decremento.
        ISala salaClonata = salaSelezionata.clone();
        ISpettacolo nuovoSpettacolo = new Spettacolo(filmSelezionato, salaClonata, orarioSelezionato);

        ICommand aggiungiSpettacoloCommand = new AggiungiSpettacoloCommand(servizioAggiungiSpettacolo, nuovoSpettacolo);
        amministratore.setCommand(aggiungiSpettacoloCommand);
        amministratore.eseguiComando();
        onSuccess.accept(nuovoSpettacolo);

        spettacoloDataSerializer.salvaSpettacolo(spettacoli);
    }


    public void rimuoviSpettacolo(long idSpettacolo, Consumer<Long> onSuccess) throws Exception {
        IRimuoviSpettacolo servizioRimuoviSpettacolo = new RimuoviSpettacolo(spettacoli);
        ICommand rimuoviSpettacoloCommand = new RimuoviSpettacoloCommand(servizioRimuoviSpettacolo, idSpettacolo);

        amministratore.setCommand(rimuoviSpettacoloCommand);
        amministratore.eseguiComando();

        onSuccess.accept(idSpettacolo); // Notifica il successo dell'operazione

        spettacoloDataSerializer.salvaSpettacolo(spettacoli);
    }

    public void modificaOrarioSpettacolo(long idSpettacolo, LocalDateTime nuovoOrario, Runnable onSuccess) throws Exception {

        IModificaSpettacolo servizioModificaOrarioSpettacolo = new ModificaSpettacolo(spettacoli);

        // Crea il comando di modifica spettacolo
        ICommand modificaOrarioSpettacoloCommand = new ModificaOrarioPerIdSpettacoloCommand(servizioModificaOrarioSpettacolo, idSpettacolo, nuovoOrario);

        amministratore.setCommand(modificaOrarioSpettacoloCommand);
        amministratore.eseguiComando();

        onSuccess.run(); // Esegue l'azione di successo, e l'aggiornamento dell'UI

        spettacoloDataSerializer.salvaSpettacolo(spettacoli);
    }

    public void modificaSalaSpettacolo(long idSpettacolo, ISala nuovaSala, Runnable onSuccess) throws Exception {

        IModificaSpettacolo servizioModificaSpettacolo = new ModificaSpettacolo(spettacoli);
        ICommand modificaSalaSpettacoloCommand = new ModificaSalaPerIdSpettacoloCommand(servizioModificaSpettacolo, idSpettacolo, nuovaSala);

        amministratore.setCommand(modificaSalaSpettacoloCommand);
        amministratore.eseguiComando();

        onSuccess.run();

        spettacoloDataSerializer.salvaSpettacolo(spettacoli);
    }

    public void modificaFilmSpettacolo(long idSpettacolo, IFilm nuovoFilm, Runnable onSuccess) throws Exception {


        IModificaSpettacolo servizioModificaSpettacolo = new ModificaSpettacolo(spettacoli);
        ICommand modificaFilmSpettacoloCommand = new ModificaFilmPerIdSpettacoloCommand(servizioModificaSpettacolo, idSpettacolo, nuovoFilm);

        amministratore.setCommand(modificaFilmSpettacoloCommand);
        amministratore.eseguiComando();

        onSuccess.run();

        spettacoloDataSerializer.salvaSpettacolo(spettacoli);
    }
}
