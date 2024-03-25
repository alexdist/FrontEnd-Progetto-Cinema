package org.example.interfaccia_grafica.service_userdashcontroller;

import Serializzazione.adapter.adaptee.PrezziBigliettoSerializer;
import Serializzazione.adapter.adapter.PrezziBigliettoSerializerAdapter;
import Serializzazione.adapter.target.IDataSerializer;
import cinema_Infrastructure.spettacolo.ISpettacolo;
import cinema_Infrastructure.spettacolo.Spettacolo;
import domain.Utente;
import id_generator_factory.product.IGeneratoreID;
import org.example.interfaccia_grafica.general_utility_classes.AlertUtil;
import prova_id_PERSISTENTE.IGeneratoreIDPersistente;
import ticket.factory.abstract_factory.BigliettoFactory;
import ticket.factory.concrete_factory.BigliettoInteroFactory;
import ticket.factory.concrete_factory.BigliettoRidottoFactory;
import ticket.factory.product.IBiglietto;
import ticket_pricing.IPrezziBiglietto;
import ticket_pricing.PrezziBiglietto;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GestoreAcquisti implements IGestoreAcquisti{
    private IGeneratoreIDPersistente generatoreID;
    private IPrezziBiglietto prezziBiglietto;

    private IPrezziBiglietto prezziBigliettoBase;
    private IDataSerializer prezziBigliettoSerializer;

    private ServizioPrezziBiglietto servizioPrezziBiglietto;

    public GestoreAcquisti(IGeneratoreIDPersistente generatoreID, IPrezziBiglietto prezziBiglietto) {
        this.generatoreID = generatoreID;
        this.prezziBiglietto = prezziBiglietto;
        this.servizioPrezziBiglietto = new ServizioPrezziBiglietto();
    }



    public List<IBiglietto> procediAcquisto(ISpettacolo spettacoloSelezionato, Utente utente, int numeroBiglietti) {
        List<IBiglietto> bigliettiCreati = new ArrayList<>();

        // Verifica condizioni di base per procedere all'acquisto
        if (spettacoloSelezionato == null || spettacoloSelezionato.getSala().getPostiDisponibili() == 0 || numeroBiglietti > spettacoloSelezionato.getSala().getPostiDisponibili()) {
            return bigliettiCreati;
        }

        // Calcola il prezzo finale per biglietto usando il servizioPrezziBiglietto
        double prezzoPerBiglietto = servizioPrezziBiglietto.calcolaPrezzoFinale(spettacoloSelezionato, utente, numeroBiglietti) / numeroBiglietti;

        BigliettoFactory bigliettoFactory = utente.getEta() < 14 ? new BigliettoRidottoFactory(generatoreID) : new BigliettoInteroFactory(generatoreID);

        for (int i = 0; i < numeroBiglietti; i++) {
            IBiglietto biglietto = bigliettoFactory.creaBiglietto(spettacoloSelezionato, utente, prezzoPerBiglietto);
            bigliettiCreati.add(biglietto);
        }

        return bigliettiCreati;
    }
}