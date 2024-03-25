package org.example.interfaccia_grafica.service_userdashcontroller;

import cinema_Infrastructure.spettacolo.ISpettacolo;
import domain.Utente;
import ticket_pricing.IPrezziBiglietto;

public interface IServizioPrezziBiglietto {
    public void applicaStrategieDiPrezzo(ISpettacolo spettacolo);
    public double calcolaPrezzoFinale(ISpettacolo spettacolo, Utente utente, int numeroBiglietti);
    public void resetPrezziBigliettoAiValoriDefault();
    public IPrezziBiglietto getPrezziBiglietto();
}
