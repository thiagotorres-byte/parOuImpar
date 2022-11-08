package br.anhembi.game;

public enum GameMode {

    SIGLEPLAYER("2"),
    MULTIPLAYER("1"),
    SAIR("3");

    private String value;

    public String getValue(){
        return this.value;
    }

    GameMode(String s) {
        this.value = s;
    }
}
