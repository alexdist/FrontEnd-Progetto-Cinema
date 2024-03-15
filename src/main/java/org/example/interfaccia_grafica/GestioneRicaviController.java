package org.example.interfaccia_grafica;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class GestioneRicaviController {
    @FXML
    private TableColumn<?, ?> affluenzaSalaCol_tableview;

    @FXML
    private Button generaRepRicavi_btn;

    @FXML
    private TableColumn<?, ?> ricaviRicavoCol_tableview;

    @FXML
    private TableColumn<?, ?> ricaviSalaCol_tableview;

    @FXML
    private TableView<?> ricavi_tableview;

}
