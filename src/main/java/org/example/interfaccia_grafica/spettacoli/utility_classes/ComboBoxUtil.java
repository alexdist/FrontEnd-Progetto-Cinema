package org.example.interfaccia_grafica.spettacoli.utility_classes;

import cinema_Infrastructure.film.IFilm;
import cinema_Infrastructure.sala.ISala;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;

public class ComboBoxUtil {

    public static void setupFilmComboBox(ComboBox<IFilm> comboBox, ObservableList<IFilm> items) {
        comboBox.setItems(items);
        comboBox.setCellFactory(cb -> new ListCell<IFilm>() {
            @Override
            protected void updateItem(IFilm item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null || empty ? null : item.getTitolo());
            }
        });
        comboBox.setButtonCell(new ListCell<IFilm>() {
            @Override
            protected void updateItem(IFilm item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? "Seleziona Film" : item.getTitolo());
            }
        });
    }

    public static void setupSalaComboBox(ComboBox<ISala> comboBox, ObservableList<ISala> items) {
        comboBox.setItems(items);
        comboBox.setCellFactory(cb -> new ListCell<ISala>() {
            @Override
            protected void updateItem(ISala item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null || empty ? null : String.valueOf(item.getNumeroSala()));
            }
        });
        comboBox.setButtonCell(new ListCell<ISala>() {
            @Override
            protected void updateItem(ISala item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? "Seleziona Sala" : String.valueOf(item.getNumeroSala()));
            }
        });
    }
}
