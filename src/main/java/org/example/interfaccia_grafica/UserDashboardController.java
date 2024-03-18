package org.example.interfaccia_grafica;

import Serializzazione.adapter.adaptee.SpettacoloSerializer;
import Serializzazione.adapter.adapter.SpettacoloSerializerAdapter;
import Serializzazione.adapter.target.IDataSerializer;
import cinema_Infrastructure.spettacolo.ISpettacolo;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;

import java.io.IOException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;



public class UserDashboardController {
    @FXML
    private Button close;

    @FXML
    private Button esci_btn;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private Button minimize;

    @FXML
    private AnchorPane topForm_anchorpane;

    @FXML
    private double x = 0;
    private double y = 0;



    @FXML
    private ListView<ISpettacolo> listViewSpettacoli;

    public void initialize() {
//        //SpettacoloSerializer spettacoloSerializer = new SpettacoloSerializer();
//        IDataSerializer spettacoloSerializerAdapter = new SpettacoloSerializerAdapter(new SpettacoloSerializer());
//
//        // Carica gli spettacoli cinematografici con un cast esplicito
//        Object result = spettacoloSerializerAdapter.deserialize("spettacoli.ser");
//        if (result instanceof List<?>) {
//            List<?> resultList = (List<?>) result;
//            if (resultList.size() > 0 && resultList.get(0) instanceof ISpettacolo) {
//                List<ISpettacolo> spettacoli = (List<ISpettacolo>) resultList;
//                // Imposta i dati nella ListView
//                listViewSpettacoli.setItems(FXCollections.observableArrayList(spettacoli));
//            } else {
//                System.out.println("La lista deserializzata non contiene elementi di tipo ISpettacolo");
//            }
//        } else {
//            System.out.println("Il risultato della deserializzazione non è una lista");
//        }
    }

    public void logout() throws IOException {
        esci_btn.getScene().getWindow().hide();
        try{
            Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));

            Stage stage = new Stage();
            Scene scene = new Scene(root);

            root.setOnMousePressed((MouseEvent event)->{

                x = event.getSceneX();
                y = event.getSceneY();

            });

            root.setOnMouseDragged((MouseEvent event)->{

                stage.setX(event.getScreenX() - x);
                stage.setY(event.getScreenY() - y);

            });

            stage.setScene(scene);
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void closeDashboard(){

        System.exit(0);
    }

    public void minimizeDashboard(){

        Stage stage = (Stage)topForm_anchorpane.getScene().getWindow();
        stage.setIconified(true);
    }
}
