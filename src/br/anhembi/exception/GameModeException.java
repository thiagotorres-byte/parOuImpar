package br.anhembi.exception;

public class GameModeException extends RuntimeException{

    public GameModeException(){
        System.out.println("Game Mode não definido");
    }

}
