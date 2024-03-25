package org.example.interfaccia_grafica.general_utility_classes.serializzazione;

import cinema_Infrastructure.spettacolo.ISpettacolo;

import java.util.List;

public interface ISpettacoloDataSerializer {
    public List<ISpettacolo> caricaSpettacoli();
}
