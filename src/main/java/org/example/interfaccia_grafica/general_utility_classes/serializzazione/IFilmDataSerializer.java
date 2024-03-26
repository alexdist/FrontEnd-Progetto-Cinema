package org.example.interfaccia_grafica.general_utility_classes.serializzazione;

import cinema_Infrastructure.film.IFilm;

import java.util.List;

public interface IFilmDataSerializer {

    public List<IFilm> caricaFilm();
    public void salvaSala(List<IFilm> filmList);
}
