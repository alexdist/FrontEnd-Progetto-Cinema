package org.example.interfaccia_grafica.general_utility_classes.serializzazione;

import Serializzazione.adapter.target.IDataSerializer;
import ticket_pricing.IPrezziBiglietto;
import java.util.Optional;

public class PrezziDataSerializer implements IPrezziDataSerializer{
    private IDataSerializer prezziSerializerAdapter;

    public PrezziDataSerializer(IDataSerializer prezziSerializerAdapter) {
        this.prezziSerializerAdapter = prezziSerializerAdapter;
    }

    public void salvaPrezzi(IPrezziBiglietto prezziBiglietto) {
        try {
            prezziSerializerAdapter.serialize(prezziBiglietto, "prezziBiglietto.ser");
        } catch (Exception e) {
            // Gestire l'eccezione, ad esempio mostrando un messaggio di errore all'utente
            e.printStackTrace(); // Per semplicità, qui stampiamo lo stack trace dell'eccezione
        }
    }

    public Optional<IPrezziBiglietto> caricaPrezzi() {
        try {
            Object result = prezziSerializerAdapter.deserialize("prezziBiglietto.ser");
            if (result instanceof IPrezziBiglietto) {
                return Optional.of((IPrezziBiglietto) result);
            } else {
                // Gestire il caso in cui il risultato non sia del tipo atteso
                return Optional.empty();
            }
        } catch (Exception e) {
            // Gestire le eccezioni, come file non trovato o errori di deserializzazione
            e.printStackTrace(); // Per semplicità, qui stampiamo lo stack trace dell'eccezione
            return Optional.empty();
        }
    }
}
