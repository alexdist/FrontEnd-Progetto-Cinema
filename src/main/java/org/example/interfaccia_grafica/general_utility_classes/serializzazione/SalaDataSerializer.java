package org.example.interfaccia_grafica.general_utility_classes.serializzazione;

import Serializzazione.adapter.target.IDataSerializer;
import cinema_Infrastructure.film.IFilm;
import cinema_Infrastructure.sala.ISala;

import java.util.ArrayList;
import java.util.List;

public class SalaDataSerializer implements ISalaDataSerializer{

    private IDataSerializer salaSerializerAdapter;

    public SalaDataSerializer(IDataSerializer salaSerializerAdapter) {
        this.salaSerializerAdapter = salaSerializerAdapter;
    }

    public List<ISala> caricaSala() {
        Object result = salaSerializerAdapter.deserialize("sale.ser");
        if (result instanceof List<?>) {
            return (List<ISala>) result;
        } else {

            return new ArrayList<>();
        }
    }


    public void salvaSala(List<ISala> saleList) {

        salaSerializerAdapter.serialize(saleList, "sale.ser");
    }
}
