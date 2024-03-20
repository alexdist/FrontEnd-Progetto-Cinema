package org.example.interfaccia_grafica.ricavi;


    public class DatiSala {
        private String nomeSala;
        private int affluenza;
        private double ricavi;

        public DatiSala(String nomeSala, int affluenza, double ricavi) {
            this.nomeSala = nomeSala;
            this.affluenza = affluenza;
            this.ricavi = ricavi;
        }

        public String getNomeSala() {
            return nomeSala;
        }

        public void setNomeSala(String nomeSala) {
            this.nomeSala = nomeSala;
        }

        public int getAffluenza() {
            return affluenza;
        }

        public void setAffluenza(int affluenza) {
            this.affluenza = affluenza;
        }

        public double getRicavi() {
            return ricavi;
        }

        public void setRicavi(double ricavi) {
            this.ricavi = ricavi;
        }
    }

