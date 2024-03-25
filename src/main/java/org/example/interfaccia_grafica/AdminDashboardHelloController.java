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

    private int getNumeroSpettacoliInPalinsesto(){
        try{
            SpettacoloSerializer spettacoloSerializer = new SpettacoloSerializer();
            IDataSerializer spettacoloSerializerAdapter = new SpettacoloSerializerAdapter(spettacoloSerializer);
            List<ISpettacolo> spettacoli = (List<ISpettacolo>) spettacoloSerializerAdapter.deserialize("spettacoli.ser");
            return spettacoli.size();
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

        private int getNumeroFilmInProgrammazione(){
        try{
            FilmSerializer filmSerializer = new FilmSerializer();
            IDataSerializer filmSerializerAdapter = new FilmSerializerAdapter(filmSerializer);
            List<IFilm> films = (List<IFilm>) filmSerializerAdapter.deserialize("film.ser");
            return films.size();
        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }
        }

        private int getNumeroSaleAttive() {
            try {
                // Utilizza l'adapter per deserializzare la lista delle sale dal file
                SalaSerializer salaSerializer = new SalaSerializer();
                IDataSerializer salaSerializerAdapter = new SalaSerializerAdapter(salaSerializer);
                List<ISala> sale = (List<ISala>) salaSerializerAdapter.deserialize("sale.ser");

                // Il numero di sale attive corrisponde alla dimensione della lista
                return sale.size();
            } catch (Exception e) {
                // Gestisci eventuali eccezioni, come un file non trovato o un errore di deserializzazione
                e.printStackTrace();
                return 0; // In caso di errore, ritorna 0 o gestisci come preferisci
            }
        }



}


