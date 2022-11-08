package br.anhembi.adapter;

import br.anhembi.cliente.ClienteFacade;

import java.io.IOException;

public interface Comunicacao {

    void conectServer() throws IOException;

    void enviarJogada(String jogada);

    void enviarModoDeJogo();

    void enviarEscolhaParOuImpar();

    void encerrarConexao();

    ClienteFacade getClienteObject();
}
