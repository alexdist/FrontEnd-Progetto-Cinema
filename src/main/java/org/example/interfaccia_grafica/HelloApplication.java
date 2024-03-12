package org.example.interfaccia_grafica;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class HelloApplication extends Application {
   // @Override
//    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        stage.show();
//    }

    private double x = 0;
    private double y = 0;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent root = fxmlLoader.load(); // Carica il layout FXML
        Scene scene = new Scene(root); // Usa le dimensioni di default dal FXML

        root.setOnMousePressed((MouseEvent event)->{
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root.setOnMouseDragged((MouseEvent event)->{
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });

        //stage.setTitle("Hello!");
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}