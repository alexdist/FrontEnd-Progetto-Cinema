package org.example.interfaccia_grafica;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;




    public class GestionePrezziController {
        @FXML
        private Button impostaPrzIntero_btn;

        @FXML
        private TextField impostaPrzIntero_textflied;

        @FXML
        private Button impostaPrzRidotto_btn;

        @FXML
        private TextField impostaPrzRidotto_textflied;

        @FXML
        private TableView<?> prezzi_tableview;

        @FXML
        private TableColumn<?, ?> prezzoInteroCol_tableview;

        @FXML
        private TableColumn<?, ?> prezzoRidottoCol_tableview;
    }


