package org.example.interfaccia_grafica.service_gestioneprezzicontroller;

import Serializzazione.adapter.adaptee.PrezziBigliettoSerializer;
import Serializzazione.adapter.adapter.PrezziBigliettoSerializerAdapter;
import admin_commands.prezzi_biglietto.ImpostaSovrapprezzoCommand;
import admin_commands.prezzi_biglietto.intero.ImpostaPrezzoInteroCommand;
import admin_commands.prezzi_biglietto.ridotto.ImpostaPrezzoRidottoCommand;
import admin_interfaces.ICommand;
import domain.Amministratore;
import org.example.interfaccia_grafica.general_utility_classes.serializzazione.IPrezziDataSerializer;
import org.example.interfaccia_grafica.general_utility_classes.serializzazione.PrezziDataSerializer;
import ticket_pricing.IPrezziBiglietto;

public class PrezziService implements IPrezziService{
    private IPrezziBiglietto prezziBiglietto;
    private IPrezziDataSerializer prezziDataSerializer;
    private Amministratore amministratore;

    public PrezziService(IPrezziBiglietto prezziBiglietto, Amministratore amministratore) {
        this.prezziBiglietto = prezziBiglietto;
        this.amministratore = amministratore;
        this.prezziDataSerializer = new PrezziDataSerializer(new PrezziBigliettoSerializerAdapter(new PrezziBigliettoSerializer()));
    }

    public void impostaPrezzoIntero(double nuovoPrezzo) throws Exception {
        // Creazione del comando per cambiare il prezzo intero
        ICommand cambiaPrezzoIntero = new ImpostaPrezzoInteroCommand(this.prezziBiglietto, nuovoPrezzo);
        this.amministratore.setCommand(cambiaPrezzoIntero);
        this.amministratore.eseguiComando();
        prezziDataSerializer.salvaPrezzi(prezziBiglietto);

    }

    public void impostaPrezzoRidotto(double nuovoPrezzo) throws Exception {
        //Creazione del comando per cambiare il prezzo ridotto
        ICommand cambiaPrezzoRidotto = new ImpostaPrezzoRidottoCommand(this.prezziBiglietto, nuovoPrezzo);
        this.amministratore.setCommand(cambiaPrezzoRidotto);
        this.amministratore.eseguiComando();
        prezziDataSerializer.salvaPrezzi(prezziBiglietto);
    }

    public void impostaSovrapprezzo(double sovrapprezzo) throws Exception {
        //Creazione del comando per cambiare il sovrapprezzo weekEnd
        ICommand impostaSovrapprezzo = new ImpostaSovrapprezzoCommand(this.prezziBiglietto, sovrapprezzo);
        this.amministratore.setCommand(impostaSovrapprezzo);
        this.amministratore.eseguiComando();
        prezziDataSerializer.salvaPrezzi(prezziBiglietto);
    }

}
