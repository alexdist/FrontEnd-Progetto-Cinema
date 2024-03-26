package org.example.interfaccia_grafica.general_utility_classes.serializzazione;

import cinema_Infrastructure.sala.ISala;
import cinema_Infrastructure.spettacolo.ISpettacolo;

import java.util.List;

public interface ISalaDataSerializer {
    public List<ISala> caricaSala();
    public void salvaSala(List<ISala> saleList);
}
