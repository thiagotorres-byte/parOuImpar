import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

class Servidor {

    public static void main(String[] args) {
        final int PORT = 9876;
        String valorJogadorUm, valorJogadorDois, escolhaParOuImpar;
        Scanner palpiteJogadorUm, palpiteJogadorDois, parOuImparEscolha;
        int resultado;

        try {

            ServerSocket serverSocket = new ServerSocket(PORT);

            Socket jogadorUm = serverSocket.accept();

            System.out.println("Jogador número 1 conectou:"  + jogadorUm.getRemoteSocketAddress());

            parOuImparEscolha = new Scanner(jogadorUm.getInputStream());

            escolhaParOuImpar = parOuImparEscolha.nextLine();

            System.out.println("Aguardando segundo jogador...");

            Socket jogadorDois = serverSocket.accept();

            System.out.println("Jogador número 2 conectou:"  + jogadorDois.getRemoteSocketAddress());

            System.out.println("Começando o jogo!");

            palpiteJogadorUm = new Scanner(jogadorUm.getInputStream());

            palpiteJogadorDois = new Scanner(jogadorDois.getInputStream());

            while (true) {

                valorJogadorUm = palpiteJogadorUm.nextLine();

                valorJogadorDois = palpiteJogadorDois.nextLine();

                resultado = Integer.parseInt(valorJogadorUm + valorJogadorDois);

                if (resultado % 2 == 0){
                    if(escolhaParOuImpar.equalsIgnoreCase("par")) {
                        System.out.println("Vitória jogador um!");
                    } else {
                        System.out.println("Vitória jogador dois!");
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Erro.");
            return;
        }

    }
}
