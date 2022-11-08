package br.anhembi.exception;

public class EnviarEscolhaParOuImparException extends RuntimeException{

    public EnviarEscolhaParOuImparException(){
        System.out.println("Par ou Impar não definido");
    }
}
