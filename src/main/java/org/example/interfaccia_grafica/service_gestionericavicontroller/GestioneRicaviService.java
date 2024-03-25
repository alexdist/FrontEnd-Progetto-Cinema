package org.example.interfaccia_grafica.service_gestionericavicontroller;

import org.example.interfaccia_grafica.ricavi.DatiSala;
import revenues_observer.observable.AbstractRegistroBiglietti;
import ticket.factory.product.IBiglietto;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestioneRicaviService implements IGestioneRicaviService{

    private AbstractRegistroBiglietti registroBiglietti;

    public GestioneRicaviService() {
        try {
            caricaRegistroBiglietti();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            // Gestione dell'errore, ad esempio inizializzazione a valori di default o logging
        }
    }

    private void caricaRegistroBiglietti() throws IOException, ClassNotFoundException {
        String filePath = "registroBiglietti.ser";
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            registroBiglietti = (AbstractRegistroBiglietti) ois.readObject();
        }

        if (registroBiglietti == null) {
            System.out.println("Errore nel caricamento del registro dei biglietti.");
            // Qui potresti decidere di inizializzare registroBiglietti con un nuovo oggetto
            // registroBiglietti = new RegistroBiglietti();
        }
    }

    public List<DatiSala> calcolaDatiPerSala() {
        List<DatiSala> datiPerSala = new ArrayList<>();

        if (registroBiglietti == null) {
            System.out.println("Registro biglietti non disponibile.");
            return datiPerSala; // Ritorna lista vuota se il registro non è caricato
        }

        Map<Integer, Integer> affluenzaPerSala = new HashMap<>();
        Map<Integer, Double> ricaviPerSala = new HashMap<>();

        List<IBiglietto> biglietti = registroBiglietti.getBiglietti();
        for (IBiglietto biglietto : biglietti) {
            int numeroSala = biglietto.getSpettacolo().getSala().getNumeroSala();
            affluenzaPerSala.merge(numeroSala, 1, Integer::sum);
            ricaviPerSala.merge(numeroSala, biglietto.getCosto(), Double::sum);
        }

        affluenzaPerSala.forEach((numeroSala, affluenza) -> {
            Double ricavi = ricaviPerSala.getOrDefault(numeroSala, 0.0);
            String nomeSala = "Sala " + numeroSala;
            datiPerSala.add(new DatiSala(nomeSala, affluenza, ricavi));
        });

        return datiPerSala;
    }
}
