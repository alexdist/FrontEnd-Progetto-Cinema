package org.example.interfaccia_grafica.spettacoli.utility_classes;

import cinema_Infrastructure.film.IFilm;
import cinema_Infrastructure.sala.ISala;
import javafx.scene.control.ComboBox;

public class ComboBoxListenerUtil {
    public static void addFilmSelectionListener(ComboBox<IFilm> comboBox) {
        comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Logica da eseguire quando un film è selezionato
                System.out.println("Film selezionato: " + newValue.getTitolo());
            }
        });
    }

    public static void addSalaSelectionListener(ComboBox<ISala> comboBox) {
        comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Logica da eseguire quando una sala è selezionata
                System.out.println("Sala selezionata: " + newValue.getNumeroSala());
            }
        });
    }
}
