package org.example.interfaccia_grafica;

import Serializzazione.adapter.adaptee.FilmSerializer;
import Serializzazione.adapter.adaptee.SalaSerializer;
import Serializzazione.adapter.adaptee.SpettacoloSerializer;
import Serializzazione.adapter.adapter.FilmSerializerAdapter;
import Serializzazione.adapter.adapter.SalaSerializerAdapter;
import Serializzazione.adapter.adapter.SpettacoloSerializerAdapter;
import Serializzazione.adapter.target.IDataSerializer;
import cinema_Infrastructure.film.IFilm;
import cinema_Infrastructure.sala.ISala;
import cinema_Infrastructure.spettacolo.ISpettacolo;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.example.interfaccia_grafica.general_utility_classes.serializzazione.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class AdminDashboardHelloController implements Initializable {

        @FXML
        private Label dashboard_filmInProgTotali;

        @FXML
        private Label dashboard_saleTotali;

        @FXML
        private Label dashboard_spettInPalTotali;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int numeroSaleAttive = getNumeroSaleAttive();
        int numeroFilminProgrammazione = getNumeroFilmInProgrammazione();
        int numeroSpettacoliInPalinsesto = getNumeroSpettacoliInPalinsesto();
        dashboard_saleTotali.setText(String.valueOf(numeroSaleAttive));
        dashboard_filmInProgTotali.setText((String.valueOf(numeroFilminProgrammazione)));
        dashboard_spettInPalTotali.setText(String.valueOf(numeroSpettacoliInPalinsesto));
    }


    private int getNumeroSpettacoliInPalinsesto() {
        ISpettacoloDataSerializer spettacoloDataSerializer = new SpettacoloDataSerializer(new SpettacoloSerializerAdapter(new SpettacoloSerializer()));
        try {
            List<ISpettacolo> spettacoli = spettacoloDataSerializer.caricaSpettacoli();
            return spettacoli.size();
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

        private int getNumeroFilmInProgrammazione(){
            IFilmDataSerializer filmDataSerializer = new FilmDataSerializer(new FilmSerializerAdapter(new FilmSerializer()));
        try{
            List<IFilm> films = filmDataSerializer.caricaFilm();
            return films.size();
        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }
        }

        private int getNumeroSaleAttive() {
            ISalaDataSerializer salaDataSerializer = new SalaDataSerializer(new SalaSerializerAdapter(new SalaSerializer()));
            try {

                List<ISala> sale =salaDataSerializer.caricaSala();

                // Il numero di sale attive corrisponde alla dimensione della lista
                return sale.size();
            } catch (Exception e) {

                e.printStackTrace();
                return 0;
            }
        }
}


