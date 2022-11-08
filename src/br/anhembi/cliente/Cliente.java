package br.anhembi.cliente;

import br.anhembi.adapter.Comunicacao;
import br.anhembi.adapter.ComunicacaoImpl;
import br.anhembi.game.GameMode;
import br.anhembi.game.PainelJogo;
import br.anhembi.player.Player;
import br.anhembi.server.Servidor;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

class Cliente implements Runnable {
    private Scanner scanner;
    private Comunicacao comunicacao;
    private String mensagem;

    public static void main(String[] args) {
        try {
            Cliente cliente = new Cliente();
            cliente.start();
        } catch (IOException ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
    }

    public Cliente() {
        this.scanner = new Scanner(System.in);
    }

    public void start() throws IOException {
        try {

            do {

                Player player = new Player();

                comunicacao = new ComunicacaoImpl(player);

                comunicacao.conectServer();

                new Thread(this).start();

                PainelJogo.imprimir();

                escolheMododeJogo(player);

                escolherParOuImpar(player);

                escolheJogada();

            } while (!mensagem.equalsIgnoreCase(GameMode.SAIR.getValue()));

            System.out.println("Finalizando o programa...");

        } finally {
            comunicacao.encerrarConexao();
        }
    }

    private void escolheJogada() {
        do {
            String jogada = scanner.nextLine();
            mensagem = jogada;

            comunicacao.enviarJogada(jogada);

        } while(!mensagem.equalsIgnoreCase("sair"));
    }

    private void escolherParOuImpar(Player player) {
        String parOuImpar = scanner.nextLine();
        mensagem = parOuImpar;
        player.setEscolhaParOuImpar(parOuImpar);
        comunicacao.enviarEscolhaParOuImpar();
    }

    private void escolheMododeJogo(Player player) {
        String gameMode = scanner.nextLine();
        player.setGameMode(gameMode);
        comunicacao.enviarModoDeJogo();
    }

    @Override
    public void run() {
        String msg;
        try {
            while ((msg = comunicacao.getClienteObject().getMessage()) != null) {
                if ("sair".equalsIgnoreCase(msg))
                    return;
                System.out.println(msg);
            }
        } finally {
            comunicacao.encerrarConexao();
        }
    }
}