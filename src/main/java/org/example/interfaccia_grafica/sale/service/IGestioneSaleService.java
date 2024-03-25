package org.example.interfaccia_grafica.sale.service;

import cinema_Infrastructure.sala.ISala;
import exception.film.*;
import exception.sala.*;
import exception.spettacolo.SovrapposizioneSpettacoloException;
import exception.spettacolo.SpettacoloNonTrovatoException;

import java.io.IOException;
import java.util.function.Consumer;

public interface IGestioneSaleService {
    void aggiungiSala(int numeroSala, int capacita, Consumer<ISala> onSuccess) throws NumeroSalaNegativoException, NumeroPostiNegativoException, SalaGiaEsistenteException, FilmGiaPresenteException, DurataFilmNonValidaException, SpettacoloNonTrovatoException, TitoloVuotoException, SalaNonTrovataException, FilmNonValidoException, SovrapposizioneSpettacoloException, SalaNonValidaException, FilmNonTrovatoException, IOException;
    void rimuoviSala(long idSala, Consumer<Long> onSuccess) throws SalaNonTrovataException, FilmGiaPresenteException, DurataFilmNonValidaException, SpettacoloNonTrovatoException, TitoloVuotoException, SalaGiaEsistenteException, FilmNonValidoException, SovrapposizioneSpettacoloException, SalaNonValidaException, FilmNonTrovatoException, Exception;
}
