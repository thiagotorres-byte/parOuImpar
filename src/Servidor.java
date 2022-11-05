import java.io.IOException;
import java.net.ServerSocket;
import java.util.Objects;
import java.util.Random;

class Servidor {
    public static final int PORT = 9863;
    private ServerSocket serverSocket;
    String parOuImparEscolhaJogadorUm;

    public void start() throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println("Servidor iniciando na porta " + PORT);
        serverloop();
    }

    private void serverloop() throws IOException {

        while (true){
            ClienteObject jogadorUm = new ClienteObject(serverSocket.accept());

            if (Objects.equals(jogadorUm.getMessage(), "1")) {

                forcaEscolhaParOuImparJogadorUm(jogadorUm);

                jogadorUm.sendMessage("Aguardando segundo jogador...");

                ClienteObject jogadorDois = new ClienteObject(serverSocket.accept());

                String parOuImparEscolhaJogadorDois = parOuImparEscolhaJogadorUm.equalsIgnoreCase("par") ? "impar" : "par";

                jogadorDois.sendMessage("O jogador: " + jogadorUm.getRemoteSocketAddress() + " escolheu: "
                        + parOuImparEscolhaJogadorUm + " e agora voce e: " + parOuImparEscolhaJogadorDois);

                sendBothPlayers("Escolha um numero de 0 a 5: ", jogadorUm, jogadorDois);

                new Thread(() -> clienteLoopVsPlayer(jogadorUm, jogadorDois)).start();
            } else {

                forcaEscolhaParOuImparJogadorUm(jogadorUm);

                String parOuImparEscolhaCPU = parOuImparEscolhaJogadorUm.equalsIgnoreCase("par") ? "impar" : "par";

                jogadorUm.sendMessage("Voce escolheu " + parOuImparEscolhaJogadorUm + "a CPU e: " + parOuImparEscolhaCPU);

                new Thread(() -> clienteLoopVsCPU(jogadorUm)).start();
            }
        }

    }

    private void clienteLoopVsCPU(ClienteObject jogadorUm) {
        Random random = new Random();
        int numeroCPU;
        String msgJogadorUm;
        int vencedor;

        try {
            while ((msgJogadorUm = jogadorUm.getMessage()) != null) {
                if (msgJogadorUm.equalsIgnoreCase("sair"))
                    return;

                numeroCPU = random.nextInt(5);

                vencedor = verifyWinnerCPU(msgJogadorUm, numeroCPU);

                notifyPlayervsCPU(vencedor, jogadorUm);
            }
        } finally {
            jogadorUm.close();
        }
    }

    private void forcaEscolhaParOuImparJogadorUm (ClienteObject jogadorUm){

        do {
            jogadorUm.sendMessage("Escolha 'par' ou 'impar': ");
            parOuImparEscolhaJogadorUm = jogadorUm.getMessage();

        } while (!"par".equalsIgnoreCase(parOuImparEscolhaJogadorUm) &&
                !"impar".equalsIgnoreCase(parOuImparEscolhaJogadorUm));

    }

    private void clienteLoopVsPlayer(ClienteObject jogadorUm, ClienteObject jogadorDois){
        String msgJogadorUm;
        String msgJogadorDois;
        int vencedor;
        try {
            while ((msgJogadorUm = jogadorUm.getMessage()) != null && (msgJogadorDois = jogadorDois.getMessage()) != null) {
                if (msgJogadorUm.equalsIgnoreCase("sair") || msgJogadorDois.equalsIgnoreCase("sair"))
                    return;

                vencedor = verifyWinnerPlayer(msgJogadorUm, msgJogadorDois);

                notifyPlayervsPlayer(vencedor, jogadorUm,jogadorDois);

                sendBothPlayers("Escolha um numero de 0 a 5: ", jogadorUm, jogadorDois);
            }
        } finally {
            jogadorUm.close();
            jogadorDois.close();
        }
    }

    private void notifyPlayervsPlayer(int vencedor, ClienteObject jogadorUm, ClienteObject jogadorDois) {
        if (vencedor == 1){
            jogadorUm.sendMessage("Você ganhou a disputa de par ou impar!");
            jogadorDois.sendMessage("Você perdeu a disputa de par ou impar!");
        } else if (vencedor == 2) {
            jogadorUm.sendMessage("Você perdeu a disputa de par ou impar!");
            jogadorDois.sendMessage("Você ganhou a disputa de par ou impar!");
        } else {
            jogadorUm.sendMessage("Não foi possível identificar o ganhador =(");
            jogadorDois.sendMessage("Não foi possível identificar o ganhador =(");
        }
    }

    private void notifyPlayervsCPU(int vencedor, ClienteObject jogadorUm) {
        if (vencedor == 1){
            jogadorUm.sendMessage("Você ganhou a disputa de par ou impar!");
        } else if (vencedor == 2) {
            jogadorUm.sendMessage("Você perdeu a disputa de par ou impar!");
        } else {
            jogadorUm.sendMessage("Não foi possível identificar o ganhador =(");
        }
    }

    private int verifyWinnerPlayer(String msgJogadorUm, String msgJogadorDois) {
        int resultado = Integer.parseInt(msgJogadorUm) + Integer.parseInt(msgJogadorDois);

        if (resultado % 2 == 0){
            if(parOuImparEscolhaJogadorUm.equalsIgnoreCase("par"))
                return 1;
            else
                return 2;
        } else {
            return 0;
        }
    }

    private int verifyWinnerCPU(String msgJogadorUm, int msgCPU) {
        int resultado = Integer.parseInt(msgJogadorUm) + msgCPU;

        if (resultado % 2 == 0){
            if(parOuImparEscolhaJogadorUm.equalsIgnoreCase("par"))
                return 1;
            else
                return 2;
        } else {
            return 0;
        }
    }

    private void sendBothPlayers(String msg, ClienteObject jogadorUm, ClienteObject jogadorDois){
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

        System.out.println("Servidor finalizado");
    }
}