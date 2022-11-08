package br.anhembi.game;

import java.util.Arrays;
import java.util.List;

public class Rules {

    private static final List<String> numerosPermitidos = Arrays.asList("0", "1", "2", "3", "4", "5");

    public static boolean validaNumerosPermitidos(String numero){
        return numerosPermitidos.contains(numero);
    }

}
