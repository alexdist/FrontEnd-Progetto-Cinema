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
            // Gestisci l'errore o ritorna una lista vuota
            return new ArrayList<>();
        }
    }

    // Metodo aggiornato per salvare la lista delle sale utilizzando salaSerializerAdapter
    public void salvaSala(List<ISala> saleList) {
        // Presumendo che IDataSerializer includa un metodo per serializzare oggetti
        salaSerializerAdapter.serialize(saleList, "sale.ser"); // Utilizza il file "sale.ser" per la serializzazione
    }
}
