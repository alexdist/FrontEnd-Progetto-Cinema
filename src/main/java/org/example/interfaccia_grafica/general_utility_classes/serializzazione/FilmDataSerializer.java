package org.example.interfaccia_grafica.general_utility_classes.serializzazione;

import Serializzazione.adapter.target.IDataSerializer;
import cinema_Infrastructure.film.IFilm;
import cinema_Infrastructure.sala.ISala;
import cinema_Infrastructure.spettacolo.ISpettacolo;

import java.util.ArrayList;
import java.util.List;

public class FilmDataSerializer implements IFilmDataSerializer{

    private IDataSerializer filmSerializerAdapter;

    public FilmDataSerializer(IDataSerializer filmSerializerAdapter) {
        this.filmSerializerAdapter = filmSerializerAdapter;
    }

    public List<IFilm> caricaFilm() {
        Object result = filmSerializerAdapter.deserialize("film.ser");
        if (result instanceof List<?>) {
            return (List<IFilm>) result;
        } else {

            return new ArrayList<>();
        }
    }

    public void salvaSala(List<IFilm> filmList) {

        filmSerializerAdapter.serialize(filmList, "film.ser");
    }
}
