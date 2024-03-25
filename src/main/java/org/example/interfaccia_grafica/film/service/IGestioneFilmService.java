package org.example.interfaccia_grafica.film.service;

import cinema_Infrastructure.film.IFilm;
import exception.film.DurataFilmNonValidaException;
import exception.film.FilmGiaPresenteException;
import exception.film.FilmNonTrovatoException;
import exception.film.TitoloVuotoException;

import java.util.function.Consumer;

public interface IGestioneFilmService {
    void aggiungiFilm(String titolo, int durata, String genere, Consumer<IFilm> onSuccess) throws FilmGiaPresenteException, DurataFilmNonValidaException, TitoloVuotoException, Exception;
    void rimuoviFilm(long idFilm, Consumer<Long> onSuccess) throws FilmNonTrovatoException, Exception;
}
