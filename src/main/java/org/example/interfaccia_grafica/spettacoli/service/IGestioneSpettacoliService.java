package org.example.interfaccia_grafica.spettacoli.service;

import cinema_Infrastructure.film.IFilm;
import cinema_Infrastructure.sala.ISala;
import cinema_Infrastructure.spettacolo.ISpettacolo;

import java.time.LocalDateTime;
import java.util.function.Consumer;

public interface IGestioneSpettacoliService {

    void aggiungiSpettacolo(IFilm filmSelezionato, ISala salaSelezionata, LocalDateTime orarioSelezionato, Consumer<ISpettacolo> onSuccess) throws Exception;
    void rimuoviSpettacolo(long idSpettacolo, Consumer<Long> onSuccess) throws Exception;
    void modificaOrarioSpettacolo(long idSpettacolo, LocalDateTime nuovoOrario, Runnable onSuccess) throws Exception;
    void modificaSalaSpettacolo(long idSpettacolo, ISala nuovaSala, Runnable onSuccess) throws Exception;
    void modificaFilmSpettacolo(long idSpettacolo, IFilm nuovoFilm, Runnable onSuccess) throws Exception;
}
