package org.example.interfaccia_grafica.general_utility_classes.serializzazione;

import Serializzazione.adapter.target.IDataSerializer;
import cinema_Infrastructure.spettacolo.ISpettacolo;
import java.util.ArrayList;
import java.util.List;

public class SpettacoloDataSerializer implements ISpettacoloDataSerializer{
    private IDataSerializer spettacoloSerializerAdapter;

    public SpettacoloDataSerializer(IDataSerializer spettacoloSerializerAdapter) {
        this.spettacoloSerializerAdapter = spettacoloSerializerAdapter;
    }

    public List<ISpettacolo> caricaSpettacoli() {
        Object result = spettacoloSerializerAdapter.deserialize("spettacoli.ser");
        if (result instanceof List<?>) {
            return (List<ISpettacolo>) result;
        } else {
            // Gestisci l'errore o ritorna una lista vuota
            return new ArrayList<>();
        }
    }

    // Metodo per salvare la lista degli spettacoli
    public void salvaSpettacolo(List<ISpettacolo> spettacoli) {
        try {
            // Presumendo che spettacoloSerializerAdapter abbia un metodo per serializzare oggetti
            spettacoloSerializerAdapter.serialize(spettacoli, "spettacoli.ser");
        } catch (Exception e) {
            e.printStackTrace();
            // Qui potresti voler gestire l'errore in modo più specifico, ad esempio mostrando un messaggio all'utente
        }
    }

    // Se necessario, puoi aggiungere qui anche metodi per salvare (serializzare) gli spettacoli
}