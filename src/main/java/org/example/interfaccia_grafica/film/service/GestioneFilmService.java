package org.example.interfaccia_grafica.film.service;

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
import org.example.interfaccia_grafica.general_utility_classes.serializzazione.IFilmDataSerializer;
import ID_persistente.IGeneratoreIDPersistente;

import java.util.List;
import java.util.function.Consumer;

public class GestioneFilmService implements IGestioneFilmService {
    private List<IFilm> films;
    private IGeneratoreIDPersistente generatoreID;
    private IFilmDataSerializer filmSerializer;
    private Amministratore amministratore;

    public GestioneFilmService(List<IFilm> films, IGeneratoreIDPersistente generatoreID, IFilmDataSerializer filmSerializer, Amministratore amministratore) {
        this.films = films;
        this.generatoreID = generatoreID;
        this.filmSerializer = filmSerializer;
        this.amministratore = amministratore;
    }

    @Override
    public void aggiungiFilm(String titolo, int durata, String genere, Consumer<IFilm> onSuccess) throws FilmGiaPresenteException, DurataFilmNonValidaException, TitoloVuotoException, Exception {
        // Implementazione del metodo per aggiungere un film, incluso il controllo di validità e unicità
        IFilm nuovoFilm = new Film(titolo, durata, genere);

        IAggiungiFilm servizioAggiungiFilm = new AggiungiFilm(films, generatoreID);
        ICommand aggiungiFilmCommand = new AggiungiFilmCommand(servizioAggiungiFilm, nuovoFilm);

        amministratore.setCommand(aggiungiFilmCommand);
        amministratore.eseguiComando();

        onSuccess.accept(nuovoFilm);

        filmSerializer.salvaSala(films);
    }

    @Override
    public void rimuoviFilm(long idFilm, Consumer<Long> onSuccess) throws FilmNonTrovatoException, Exception {

        IRimuoviFilm servizioRimuoviFilm = new RimuoviFilm(films);
        ICommand rimuoviFilmCommand = new RimuoviFilmCommand(servizioRimuoviFilm, idFilm);

        amministratore.setCommand(rimuoviFilmCommand);
        amministratore.eseguiComando();


        filmSerializer.salvaSala(films);

        onSuccess.accept(idFilm);

    }
}