package org.example.interfaccia_grafica.pagamento.service;

import domain.Utente;
import payment_strategy.IMetodoPagamentoStrategy;
import ticket.factory.product.IBiglietto;

import java.io.IOException;
import java.util.List;

public interface IPagamentoService {
    boolean eseguiPagamento(List<IBiglietto> bigliettiDaAcquistare, IMetodoPagamentoStrategy metodoPagamento) throws IOException, ClassNotFoundException;
    void annullaUltimoAcquisto(List<IBiglietto> bigliettiDaAcquistare, Runnable onAnnullamentoSuccess) throws IOException, ClassNotFoundException;
    void setUtente(Utente utente);
}
