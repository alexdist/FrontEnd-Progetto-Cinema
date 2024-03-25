package org.example.interfaccia_grafica.service_userdashcontroller;

import cinema_Infrastructure.spettacolo.ISpettacolo;
import domain.Utente;
import ticket_pricing.IPrezziBiglietto;
import ticket_pricing.PrezziBiglietto;
import ticket_pricing.strategy.Context;
import ticket_pricing.strategy.PrezzoBaseStrategy;
import ticket_pricing.strategy.PrezzoWeekEndStrategy;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class ServizioPrezziBiglietto implements IServizioPrezziBiglietto{
    private IPrezziBiglietto prezziBiglietto;

    public ServizioPrezziBiglietto() {
        caricaPrezziBiglietti();
    }

    private void caricaPrezziBiglietti() {
        File file = new File("prezziBiglietto.ser");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                // Deserializza i prezzi del biglietto dal file
                prezziBiglietto = (IPrezziBiglietto) ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
                // Gestione dell'errore o inizializzazione a valori di default
                inizializzaPrezziDefault();
            }
        } else {
            // Gestione del caso in cui il file non esiste: inizializza a valori di default
            inizializzaPrezziDefault();
        }
    }

    public void applicaStrategieDiPrezzo(ISpettacolo spettacolo) {
        resetPrezziBigliettoAiValoriDefault();

        if (spettacolo != null) {

            Context context = new Context(new PrezzoBaseStrategy(prezziBiglietto));
            context.executeStrategy();

            DayOfWeek giorno = spettacolo.getOrarioProiezione().getDayOfWeek();
            if (giorno == DayOfWeek.SATURDAY || giorno == DayOfWeek.SUNDAY) {
                context.setStrategy(new PrezzoWeekEndStrategy(prezziBiglietto));
                context.executeStrategy();
            }
        }
    }


    public double calcolaPrezzoFinale(ISpettacolo spettacolo, Utente utente, int numeroBiglietti) {
        applicaStrategieDiPrezzo(spettacolo);

        double prezzoBase = utente.getEta() < 14 ? prezziBiglietto.getPrezzoRidotto() : prezziBiglietto.getPrezzoIntero();
        double prezzoFinale = prezzoBase * numeroBiglietti;
        return prezzoFinale;
    }



    private void inizializzaPrezziDefault() {
        // Qui puoi inizializzare prezziBiglietto con alcuni valori di default
        prezziBiglietto = new PrezziBiglietto(0, 0, 0); // Assumiamo questi come valori di default
    }

    public void resetPrezziBigliettoAiValoriDefault() {
        caricaPrezziBiglietti();
//        File file = new File("prezziBiglietto.ser");
//        if (file.exists()) {
//            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
//                prezziBiglietto = (IPrezziBiglietto) ois.readObject();
//            } catch (Exception e) {
//                e.printStackTrace();
//                inizializzaPrezziDefault();
//            }
//        } else {
//            inizializzaPrezziDefault();
//        }
    }


    public IPrezziBiglietto getPrezziBiglietto() {
        return prezziBiglietto;
    }
}
