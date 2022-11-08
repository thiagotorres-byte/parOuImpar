package br.anhembi.server;

import br.anhembi.cliente.ClienteFacade;

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

            ClienteFacade jogadorUm = new ClienteFacade(serverSocket.accept());

            if (Objects.equals(jogadorUm.getMessage(), "1")) {

                forcaEscolhaParOuImparJogadorUm(jogadorUm);

                jogadorUm.sendMessage("Aguardando segundo jogador...");

                ClienteFacade jogadorDois = new ClienteFacade(serverSocket.accept());

                if (Objects.equals(jogadorDois.getMessage(), "1")) {

                    String parOuImparEscolhaJogadorDois = parOuImparEscolhaJogadorUm.equalsIgnoreCase("par") ? "impar" : "par";

                    jogadorDois.sendMessage("O jogador: " + jogadorUm.getRemoteSocketAddress() + " escolheu: "
                            + parOuImparEscolhaJogadorUm + " e agora voce e: " + parOuImparEscolhaJogadorDois);

                    sendBothPlayers("Escolha um numero de 0 a 5: ", jogadorUm, jogadorDois);

                    new Thread(() -> clienteLoopVsPlayer(jogadorUm, jogadorDois)).start();
                } else {
                    jogarVsCPU(jogadorDois);
                }

            } else {
                jogarVsCPU(jogadorUm);
            }
        }

    }

    private void jogarVsCPU(ClienteFacade jogadorUm) {

        forcaEscolhaParOuImparJogadorUm(jogadorUm);

        String parOuImparEscolhaCPU = parOuImparEscolhaJogadorUm.equalsIgnoreCase("par") ? "impar" : "par";

        jogadorUm.sendMessage("A CPU e: " + parOuImparEscolhaCPU);

        jogadorUm.sendMessage("Escolha um numero de 0 a 5 ou digite 'sair'");

        new Thread(() -> clienteLoopVsCPU(jogadorUm)).start();
    }

    private void forcaEscolhaParOuImparJogadorUm (ClienteFacade jogadorUm){

        do {
            jogadorUm.sendMessage("Escolha 'par' ou 'impar': ");
            parOuImparEscolhaJogadorUm = jogadorUm.getMessage();

        } while (!"par".equalsIgnoreCase(parOuImparEscolhaJogadorUm) &&
                !"impar".equalsIgnoreCase(parOuImparEscolhaJogadorUm));

    }

    private void clienteLoopVsCPU(ClienteFacade jogadorUm) {
        Random random = new Random();
        int numeroCPU;
        String msgJogadorUm;
        int vencedor;

        try {
            while ((msgJogadorUm = jogadorUm.getMessage()) != null) {
                if (msgJogadorUm.equalsIgnoreCase("sair")) {
                    jogadorUm.close();
                    return;
                }

                numeroCPU = random.nextInt(5);

                vencedor = verifyWinner(Integer.parseInt(msgJogadorUm), numeroCPU);

                notifyPlayervsCPU(vencedor, jogadorUm, numeroCPU);

                jogadorUm.sendMessage("Escolha um numero de 0 a 5 ou digite 'sair'");

            }
        } finally {
            jogadorUm.close();
        }
    }

    private void clienteLoopVsPlayer(ClienteFacade jogadorUm, ClienteFacade jogadorDois){
        String msgJogadorUm;
        String msgJogadorDois;
        int vencedor;
        try {
            while ((msgJogadorUm = jogadorUm.getMessage()) != null && (msgJogadorDois = jogadorDois.getMessage()) != null) {
                if (msgJogadorUm.equalsIgnoreCase("sair") || msgJogadorDois.equalsIgnoreCase("sair")){
                    jogadorUm.close();
                    jogadorDois.close();
                    return;
                }

                vencedor = verifyWinner(Integer.parseInt(msgJogadorUm), Integer.parseInt(msgJogadorDois));

                notifyPlayervsPlayer(vencedor, jogadorUm, jogadorDois, msgJogadorUm, msgJogadorDois);

                sendBothPlayers("Escolha um numero de 0 a 5 ou digite 'sair'", jogadorUm, jogadorDois);
            }
        } finally {
            jogadorUm.close();
            jogadorDois.close();
        }
    }

    private void notifyPlayervsPlayer(int vencedor, ClienteFacade jogadorUm,
                                      ClienteFacade jogadorDois, String msgJogadorUm, String msgJogadorDois) {
        if (vencedor == 1){
            jogadorUm.sendMessage("Voce ganhou a disputa de par ou impar! o jogador dois jogou: " + msgJogadorDois);
            jogadorDois.sendMessage("Voce perdeu a disputa de par ou impar! o jogador um jogou: " + msgJogadorUm);
        } else if (vencedor == 2) {
            jogadorUm.sendMessage("Voce perdeu a disputa de par ou impar!o jogador dois jogou: " + msgJogadorDois );
            jogadorDois.sendMessage("Voce ganhou a disputa de par ou impar!o jogador um jogou: " + msgJogadorUm);
        } else {
            jogadorUm.sendMessage("Nao foi possivel identificar o ganhador =(");
            jogadorDois.sendMessage("Nao foi possivel identificar o ganhador =(");
        }
    }

    private void notifyPlayervsCPU(int vencedor, ClienteFacade jogadorUm, int numeroCPU) {
        if (vencedor == 1){
            jogadorUm.sendMessage("Voce ganhou a disputa de par ou impar! A CPU jogou o numero: " + numeroCPU);
        } else if (vencedor == 2) {
            jogadorUm.sendMessage("Voce perdeu a disputa de par ou impar! A CPU jogou o numero: " + numeroCPU);
        } else {
            jogadorUm.sendMessage("Nao foi possivel identificar o ganhador =(");
        }
    }

    private int verifyWinner(int msgJogadorUm, int msgJogadorDois) {
        int resultado = msgJogadorUm + msgJogadorDois;

        if (resultado % 2 == 0){
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