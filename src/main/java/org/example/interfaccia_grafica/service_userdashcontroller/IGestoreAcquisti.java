package org.example.interfaccia_grafica.service_userdashcontroller;

import cinema_Infrastructure.spettacolo.ISpettacolo;
import domain.Utente;
import ticket.factory.product.IBiglietto;

import java.util.List;

public interface IGestoreAcquisti {
    public List<IBiglietto> procediAcquisto(ISpettacolo spettacoloSelezionato, Utente utente, int numeroBiglietti);

}
