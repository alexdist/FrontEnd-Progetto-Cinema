package org.example.interfaccia_grafica.service_gestioneprezzicontroller;

public interface IPrezziService {
    public void impostaPrezzoIntero(double nuovoPrezzo) throws Exception;
    public void impostaPrezzoRidotto(double nuovoPrezzo) throws Exception;
    public void impostaSovrapprezzo(double sovrapprezzo) throws Exception;
}
