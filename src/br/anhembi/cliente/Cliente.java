package br.anhembi.cliente;

import br.anhembi.game.Game;
import br.anhembi.game.GameImpl;
import br.anhembi.game.GameMode;
import br.anhembi.game.PainelJogo;
import br.anhembi.player.Player;

import java.io.IOException;
import java.util.Scanner;

class Cliente implements Runnable {

    private Scanner scanner;
    private Game game;
    private String mensagem;

    public static void main(String[] args) {
        try {
            Cliente cliente = new Cliente();
            cliente.start();
        } catch (IOException ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
    }


    public Cliente() throws IOException {
        this.scanner = new Scanner(System.in);
    }

    public void start() throws IOException {
        try {

            new Thread(this).start();

            do {
                PainelJogo.imprimir();

                Player player = new Player();
                game.conectServer();

                game = new GameImpl(player);

                ecolheMododeJogo(player);

                escolherParOuImpar(player);

                escolheJogada();

            } while (!mensagem.equalsIgnoreCase(GameMode.SAIR.getValue()));

            System.out.println("Finalizando o programa...");

        } finally {
            game.encerrarConexao();
        }
    }

    private void escolheJogada() {
        do {
            String jogada = scanner.nextLine();
            mensagem = jogada;

            game.enviarJogada(jogada);

        } while(!mensagem.equalsIgnoreCase("sair"));
    }

    private void escolherParOuImpar(Player player) {
        String parOuImpar = scanner.nextLine();
        mensagem = parOuImpar;
        player.setEscolhaParOuImpar(parOuImpar);
        game.enviarEscolhaParOuImpar();
    }

    private void ecolheMododeJogo(Player player) {
        String gameMode = scanner.nextLine();
        player.setGameMode(gameMode);
        game.enviarModoDeJogo();
    }


    @Override
    public void run() {
        String msg;
        try {
            while ((msg = game.getClienteObject().getMessage()) != null) {
                if ("sair".equalsIgnoreCase(msg))
                    return;
                System.out.println(msg);
            }
        } finally {
            game.encerrarConexao();
        }
    }
}