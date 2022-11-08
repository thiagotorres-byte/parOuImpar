package br.anhembi.game;

import br.anhembi.cliente.ClienteFacade;
import br.anhembi.exception.EnviarEscolhaParOuImparException;
import br.anhembi.exception.GameModeException;
import br.anhembi.player.Player;

import java.io.IOException;
import java.net.Socket;

public class GameImpl implements Game {

    private Player player;
    private static final int port = 9863;
    private static final String ipAdress = "127.0.0.1";
    private ClienteFacade clienteFacade;

    public GameImpl(Player player) {
        this.player = player;
    }

    public void conectServer() throws IOException {
        try {
            clienteFacade = new ClienteFacade(
                    new Socket(ipAdress, port)
            );
        } finally {
            clienteFacade.close();
        }
    }

    public void enviarJogada(String jogada){
        if(Rules.validaNumerosPermitidos(jogada)){
            clienteFacade.sendMessage(jogada);
        } else {
            System.out.println("Você não escolheu um numero de 0 a 5... Tente novamente! ");
        }
    }

    public void enviarModoDeJogo() {
        if(this.player.getGameMode() != null) {
            clienteFacade.sendMessage(this.player.getGameMode().getValue());
        } else {
            throw new GameModeException();
        }
    }

    public void enviarEscolhaParOuImpar(){
        if(this.player.getEscolhaParOuImpar() != null) {
            clienteFacade.sendMessage(this.player.getEscolhaParOuImpar());
        } else {
            throw new EnviarEscolhaParOuImparException();
        }
    }

    public void encerrarConexao(){
        clienteFacade.close();
    }

    public ClienteFacade getClienteObject() {
        return clienteFacade;
    }
}
