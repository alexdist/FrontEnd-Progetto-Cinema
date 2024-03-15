package org.example.interfaccia_grafica;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class GestioneSpettacoliController {
    @FXML
    private TextField IDRimuoviSpett_textfield;

    @FXML
    private TableColumn<?, ?> IDSpettacoloCol_tableview;

    @FXML
    private TextField IDmodFilmSpett_textfield;

    @FXML
    private TextField IDmodOrarioSpett_textfield;

    @FXML
    private TextField IDmodSalaSpett_textfield;

    @FXML
    private AnchorPane aggElimSpett_anchorpane;

    @FXML
    private TextField aggFilmSpett_textfield;

    @FXML
    private TextField aggOrarioSpett_textfield;

    @FXML
    private TextField aggSalaSpett_textfield;

    @FXML
    private Button aggiungiSpettacolo_btn;

    @FXML
    private Button eliminaSpettacolo_btn;

    @FXML
    private TableColumn<?, ?> filmCol_tableview;

    @FXML
    private TextField modFilmSpett_textfield;

    @FXML
    private TextField modOrarioSpett_textfield;

    @FXML
    private TextField modSalaSpett_textfield;

    @FXML
    private AnchorPane modSpettacolo_anchorpane;

    @FXML
    private Button modificaFilmSpett_btn;

    @FXML
    private Button modificaOrarioSpett_btn;

    @FXML
    private Button modificaSalaSpett_btn;

    @FXML
    private TableColumn<?, ?> orarioProiezioneCol_tableview;

    @FXML
    private TableColumn<?, ?> salaCol_tableview;

    @FXML
    private TableColumn<?, ?> spettacoloCol_tableview;

    @FXML
    private TableView<?> spettacolo_tableview;

    @FXML
    private Button switchModificaSpett_btn;

    @FXML
    private Button switchTornaIndietroSpett_btn;

    @FXML
    public void switchSpettacolo(ActionEvent event){
        if(event.getSource() == switchModificaSpett_btn){
            aggElimSpett_anchorpane.setVisible(false);
            modSpettacolo_anchorpane.setVisible(true);

        }else if(event.getSource() == switchTornaIndietroSpett_btn){
            aggElimSpett_anchorpane.setVisible(true);
            modSpettacolo_anchorpane.setVisible(false);
        }
    }

}
