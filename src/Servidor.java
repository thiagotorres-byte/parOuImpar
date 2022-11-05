import java.io.IOException;
import java.net.ServerSocket;

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

            forcaEscolhaParOuImparJogadorUm(jogadorUm);

            jogadorUm.sendMessage("Aguardando segundo jogador...");

            ClienteObject jogadorDois = new ClienteObject(serverSocket.accept());

            String parOuImparEscolhaJogadorDois = parOuImparEscolhaJogadorUm.equalsIgnoreCase("par") ? "impar" : "par";

            jogadorDois.sendMessage("O jogador: " + jogadorUm.getRemoteSocketAddress() + " escolheu: "
                    + parOuImparEscolhaJogadorUm + " e agora voce e: " + parOuImparEscolhaJogadorDois);

            sendBothPlayers("Escolha um numero de 0 a 5: ", jogadorUm, jogadorDois);

            new Thread(() -> clienteLoop(jogadorUm, jogadorDois)).start();
        }

    }

    private void forcaEscolhaParOuImparJogadorUm (ClienteObject jogadorUm){

        do {
            jogadorUm.sendMessage("Escolha 'par' ou 'impar': ");
            parOuImparEscolhaJogadorUm = jogadorUm.getMessage();

        } while (!"par".equalsIgnoreCase(parOuImparEscolhaJogadorUm) &&
                !"impar".equalsIgnoreCase(parOuImparEscolhaJogadorUm));

    }

    private void clienteLoop(ClienteObject jogadorUm, ClienteObject jogadorDois){
        String msgJogadorUm;
        String msgJogadorDois;
        int vencedor;
        try {
            while ((msgJogadorUm = jogadorUm.getMessage()) != null && (msgJogadorDois = jogadorDois.getMessage()) != null) {
                if (msgJogadorUm.equalsIgnoreCase("sair") || msgJogadorDois.equalsIgnoreCase("sair"))
                    return;

                vencedor = verifyWinner(msgJogadorUm, msgJogadorDois);

                notifyPlayers(vencedor, jogadorUm,jogadorDois);

                sendBothPlayers("Escolha um numero de 0 a 5: ", jogadorUm, jogadorDois);
            }
        } finally {
            jogadorUm.close();
            jogadorDois.close();
        }
    }

    private void notifyPlayers(int vencedor, ClienteObject jogadorUm, ClienteObject jogadorDois) {
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

    private int verifyWinner(String msgJogadorUm, String msgJogadorDois) {
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