package br.anhembi.server;

import br.anhembi.cliente.ClienteFacade;
import br.anhembi.game.GameMode;
import br.anhembi.player.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Objects;
import java.util.Random;

public class Servidor {
    public static final int PORT = 9863;
    private ServerSocket serverSocket;
    String parOuImparEscolhaJogadorUm;

    public void start() throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println("br.anhembi.server.Servidor iniciando na porta " + PORT);
        serverloop();
    }

    private void serverloop() throws IOException {

        while (true){

            Player jogadorUm = new Player(new ClienteFacade(serverSocket.accept()));

            if (Objects.equals(jogadorUm.getClienteFacade().getMessage(), GameMode.MULTIPLAYER.getValue())) {

                forcaEscolhaParOuImparJogadorUm(jogadorUm.getClienteFacade());

                jogadorUm.getClienteFacade().sendMessage("Aguardando segundo jogador...");

                Player jogadorDois = new Player(new ClienteFacade(serverSocket.accept()));

                if (Objects.equals(jogadorDois.getClienteFacade().getMessage(), GameMode.MULTIPLAYER.getValue())) {

                    String parOuImparEscolhaJogadorDois = parOuImparEscolhaJogadorUm.equalsIgnoreCase("par") ? "impar" : "par";

                    jogadorDois.getClienteFacade().sendMessage("O jogador: " + jogadorUm.getClienteFacade()
                            .getRemoteSocketAddress() + " escolheu: " + parOuImparEscolhaJogadorUm +
                            " e agora voce e: " + parOuImparEscolhaJogadorDois);

                    sendBothPlayers("Escolha um numero de 0 a 5: ", jogadorUm.getClienteFacade(),
                            jogadorDois.getClienteFacade());

                    new Thread(() -> clienteLoopVsPlayer(jogadorUm, jogadorDois))
                            .start();

                } else {
                    jogarVsCPU(jogadorDois);
                }

            } else {
                jogarVsCPU(jogadorUm);
            }
        }
    }

    private void jogarVsCPU(Player jogadorUm) {

        forcaEscolhaParOuImparJogadorUm(jogadorUm.getClienteFacade());

        String parOuImparEscolhaCPU = parOuImparEscolhaJogadorUm.equalsIgnoreCase("par") ? "impar" : "par";

        jogadorUm.getClienteFacade().sendMessage("A CPU e: " + parOuImparEscolhaCPU);

        jogadorUm.getClienteFacade().sendMessage("Escolha um numero de 0 a 5 ou digite 'sair'");

        new Thread(() -> clienteLoopVsCPU(jogadorUm)).start();
    }

    private void forcaEscolhaParOuImparJogadorUm (ClienteFacade jogadorUm){

        do {
            jogadorUm.sendMessage("Escolha 'par' ou 'impar': ");
            parOuImparEscolhaJogadorUm = jogadorUm.getMessage();

        } while (!"par".equalsIgnoreCase(parOuImparEscolhaJogadorUm) &&
                !"impar".equalsIgnoreCase(parOuImparEscolhaJogadorUm));

    }

    private void clienteLoopVsCPU(Player jogadorUm) {
        Random random = new Random();
        int numeroCPU;
        String msgJogadorUm;
        int vencedor;

        try {
            while ((msgJogadorUm = jogadorUm.getClienteFacade().getMessage()) != null) {
                if (msgJogadorUm.equalsIgnoreCase("sair")) {
                    jogadorUm.getClienteFacade().close();
                    return;
                }

                numeroCPU = random.nextInt(5);

                vencedor = verifyWinner(Integer.parseInt(msgJogadorUm), numeroCPU);

                notifyPlayervsCPU(vencedor, jogadorUm, numeroCPU);

                jogadorUm.getClienteFacade().sendMessage("Escolha um numero de 0 a 5 ou digite 'sair'");

            }
        } finally {

            jogadorUm.getClienteFacade().sendMessage("Você ganhou " + jogadorUm.getPontuacao() + " partidas");

            jogadorUm.getClienteFacade().close();
        }
    }

    private void clienteLoopVsPlayer(Player jogadorUm, Player jogadorDois){
        String msgJogadorUm;
        String msgJogadorDois;
        int vencedor;
        try {
            while ((msgJogadorUm = jogadorUm.getClienteFacade().getMessage()) != null
                    && (msgJogadorDois = jogadorDois.getClienteFacade().getMessage()) != null) {
                if (msgJogadorUm.equalsIgnoreCase("sair") || msgJogadorDois.equalsIgnoreCase("sair")){
                    jogadorUm.getClienteFacade().close();
                    jogadorDois.getClienteFacade().close();
                    return;
                }

                vencedor = verifyWinner(Integer.parseInt(msgJogadorUm), Integer.parseInt(msgJogadorDois));

                notifyPlayervsPlayer(vencedor, jogadorUm, jogadorDois, msgJogadorUm, msgJogadorDois);

                sendBothPlayers("Escolha um numero de 0 a 5 ou digite 'sair'",
                        jogadorUm.getClienteFacade(),
                        jogadorDois.getClienteFacade());
            }
        } finally {
            jogadorUm.getClienteFacade().sendMessage("Você ganhou " + jogadorUm.getPontuacao() + " partidas");
            jogadorDois.getClienteFacade().sendMessage("Você ganhou " + jogadorUm.getPontuacao() + " partidas");
            jogadorUm.getClienteFacade().close();
            jogadorDois.getClienteFacade().close();
        }
    }

    private void notifyPlayervsPlayer(int vencedor, Player jogadorUm,
                                      Player jogadorDois, String msgJogadorUm, String msgJogadorDois) {
        if (vencedor == 1){
            jogadorUm.getClienteFacade().sendMessage("Voce ganhou a disputa de par ou impar! o jogador dois jogou: " + msgJogadorDois);
            jogadorDois.getClienteFacade().sendMessage("Voce perdeu a disputa de par ou impar! o jogador um jogou: " + msgJogadorUm);
            jogadorUm.ganhouPartida();
        } else if (vencedor == 2) {
            jogadorUm.getClienteFacade().sendMessage("Voce perdeu a disputa de par ou impar!o jogador dois jogou: " + msgJogadorDois );
            jogadorDois.getClienteFacade().sendMessage("Voce ganhou a disputa de par ou impar!o jogador um jogou: " + msgJogadorUm);
            jogadorDois.ganhouPartida();
        } else {
            jogadorUm.getClienteFacade().sendMessage("Nao foi possivel identificar o ganhador =(");
            jogadorDois.getClienteFacade().sendMessage("Nao foi possivel identificar o ganhador =(");
        }
    }

    private void notifyPlayervsCPU(int vencedor, Player jogadorUm, int numeroCPU) {
        if (vencedor == 1){
            jogadorUm.getClienteFacade().sendMessage("Voce ganhou a disputa de par ou impar! A CPU jogou o numero: " + numeroCPU);
            jogadorUm.ganhouPartida();
        } else if (vencedor == 2) {
            jogadorUm.getClienteFacade().sendMessage("Voce perdeu a disputa de par ou impar! A CPU jogou o numero: " + numeroCPU);
        } else {
            jogadorUm.getClienteFacade().sendMessage("Nao foi possivel identificar o ganhador =(");
        }
    }

    private int verifyWinner(int msgJogadorUm, int msgJogadorDois) {
        int resultado = msgJogadorUm + msgJogadorDois;

        if (isPar(resultado)){
            if(parOuImparEscolhaJogadorUm.equalsIgnoreCase("par"))
                return 1;
            else
                return 2;
        } else {
            if(parOuImparEscolhaJogadorUm.equalsIgnoreCase("impar"))
                return 1;
            else
                return 2;
        }
    }

    private void sendBothPlayers(String msg, ClienteFacade jogadorUm, ClienteFacade jogadorDois){
        jogadorUm.sendMessage(msg);
        jogadorDois.sendMessage(msg);
    }

    private Boolean isPar(int numero){
        return numero % 2 == 0;
    }

    public static void main(String[] args) {
        try{
            Servidor server = new Servidor();
            server.start();
        } catch (IOException ex){
            System.out.println("Erro: " + ex.getMessage());
        }

        System.out.println("br.anhembi.server.Servidor finalizado");
    }
}