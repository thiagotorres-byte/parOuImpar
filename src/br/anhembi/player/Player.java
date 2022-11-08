package br.anhembi.player;

import br.anhembi.ClienteFacade;
import br.anhembi.game.GameMode;

public class Player {

    private GameMode gameMode;

    private static int pontuacao;
    private String escolhaParOuImpar;
    private ClienteFacade clienteFacade;

    public Player(GameMode gameMode) {
        this.gameMode = gameMode;
        this.pontuacao = 0;
    }

    public Player(ClienteFacade clienteFacade) {
        this.clienteFacade = clienteFacade;
        this.pontuacao = 0;
    }

    public Player() {
        this.pontuacao = 0;
    }


    public String getEscolhaParOuImpar() {
        return escolhaParOuImpar;
    }

    public void setGameMode(String gameModeString) {
        if(gameModeString.equalsIgnoreCase(GameMode.MULTIPLAYER.getValue())){
            this.gameMode = GameMode.MULTIPLAYER;
        } else {
            this.gameMode = GameMode.SIGLEPLAYER;
        }
    }

    public void setEscolhaParOuImpar(String escolhaParOuImpar) {
        this.escolhaParOuImpar = escolhaParOuImpar;
    }

    public GameMode getGameMode() {
        return gameMode;
    }
}
