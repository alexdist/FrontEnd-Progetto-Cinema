package org.example.interfaccia_grafica.film.service;

import Serializzazione.adapter.target.IDataSerializer;
import admin_commands.film.aggiungi_film.AggiungiFilmCommand;
import admin_commands.film.elimina_film.RimuoviFilmCommand;
import admin_interfaces.ICommand;
import cinema_Infrastructure.film.Film;
import cinema_Infrastructure.film.IFilm;
import cinema_Infrastructure.film.gestione_film.AggiungiFilm;
import cinema_Infrastructure.film.gestione_film.IAggiungiFilm;
import cinema_Infrastructure.film.gestione_film.IRimuoviFilm;
import cinema_Infrastructure.film.gestione_film.RimuoviFilm;
import domain.Amministratore;
import exception.film.DurataFilmNonValidaException;
import exception.film.FilmGiaPresenteException;
import exception.film.FilmNonTrovatoException;
import exception.film.TitoloVuotoException;
import prova_id_PERSISTENTE.IGeneratoreIDPersistente;

import java.util.List;
import java.util.function.Consumer;

public class GestioneFilmService implements IGestioneFilmService {
    private List<IFilm> films;
    private IGeneratoreIDPersistente generatoreID;
    private IDataSerializer filmSerializer;
    private Amministratore amministratore;

    public GestioneFilmService(List<IFilm> films, IGeneratoreIDPersistente generatoreID, IDataSerializer filmSerializer, Amministratore amministratore) {
        this.films = films;
        this.generatoreID = generatoreID;
        this.filmSerializer = filmSerializer;
        this.amministratore = amministratore;
    }

    @Override
    public void aggiungiFilm(String titolo, int durata, String genere, Consumer<IFilm> onSuccess) throws FilmGiaPresenteException, DurataFilmNonValidaException, TitoloVuotoException, Exception {
        // Implementazione del metodo per aggiungere un film, incluso il controllo di validità e unicità
        IFilm nuovoFilm = new Film(titolo, durata, genere);
        // Assumi che Film sia una classe che implementa IFilm
        // Eseguire qui la logica per verificare l'unicità, la validità dei dati e aggiungere il film alla lista

        IAggiungiFilm servizioAggiungiFilm = new AggiungiFilm(films, generatoreID);
        ICommand aggiungiFilmCommand = new AggiungiFilmCommand(servizioAggiungiFilm, nuovoFilm);

        amministratore.setCommand(aggiungiFilmCommand);
        amministratore.eseguiComando();

        onSuccess.accept(nuovoFilm);
        filmSerializer.serialize(films, "film.ser");
    }

    @Override
    public void rimuoviFilm(long idFilm, Consumer<Long> onSuccess) throws FilmNonTrovatoException, Exception {

        IRimuoviFilm servizioRimuoviFilm = new RimuoviFilm(films);
        ICommand rimuoviFilmCommand = new RimuoviFilmCommand(servizioRimuoviFilm, idFilm);
        // Implementazione del metodo per rimuovere un film
        amministratore.setCommand(rimuoviFilmCommand);
        amministratore.eseguiComando();

        filmSerializer.serialize(films, "film.ser");

        onSuccess.accept(idFilm);

    }
}