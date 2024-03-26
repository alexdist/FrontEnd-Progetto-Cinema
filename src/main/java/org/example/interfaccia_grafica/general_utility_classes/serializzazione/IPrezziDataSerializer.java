package org.example.interfaccia_grafica.general_utility_classes.serializzazione;

import ticket_pricing.IPrezziBiglietto;

import java.util.Optional;

public interface IPrezziDataSerializer {
    public void salvaPrezzi(IPrezziBiglietto prezziBiglietto);
    public Optional<IPrezziBiglietto> caricaPrezzi();
}
