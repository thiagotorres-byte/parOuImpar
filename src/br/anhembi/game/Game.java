package br.anhembi.game;

import br.anhembi.cliente.ClienteFacade;

import java.io.IOException;

public interface Game {

    void conectServer() throws IOException;

    void enviarJogada(String jogada);

    void enviarModoDeJogo();

    void enviarEscolhaParOuImpar();

    void encerrarConexao();

    ClienteFacade getClienteObject();
}
