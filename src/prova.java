import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

class Cliente1 {
    public static void main(String[] args) {
        final String IP = "127.0.0.1";
        final int PORT = 9876;
        String msg;

        try {

            Socket socket = new Socket(IP, PORT);

            Scanner scanner = new Scanner(System.in);

            OutputStream out = socket.getOutputStream();

            PrintStream printStream = new PrintStream(out);

            do {

                msg = scanner.nextLine();

                printStream.println(msg);

            } while (msg != "sair");

            socket.close();
            scanner.close();
            printStream.close();

        } catch (Exception e) {
            System.out.println("Erro.");
            return;
        }
    }
}

class Cliente2 {
    public static void main(String[] args) {
        final String IP = "127.0.0.1";
        final int PORT = 9876;
        String msg;

        try {

            Socket socket = new Socket(IP, PORT);

            Scanner scanner = new Scanner(System.in);

            OutputStream out = socket.getOutputStream();

            PrintStream printStream = new PrintStream(out);

            do {

                msg = scanner.nextLine();

                printStream.println(msg);

            } while (msg != "sair");

            socket.close();
            scanner.close();
            printStream.close();

        } catch (Exception e) {
            System.out.println("Erro.");
            return;
        }
    }
}

class Servidor {

    public static void main(String[] args) {
        final int PORT = 9876;
        String msg, valorJogadorUm, valorJogadorDois;
        Scanner scanner = new Scanner(System.in);

        PainelJogo.imprimir();

        String escolhaDoJogador = scanner.nextLine();

        if (escolhaDoJogador.equals("1")){
            try {

                System.out.println("Começando modo Jogador vs Jogador");

                ServerSocket serverSocket = new ServerSocket(PORT);

                Socket socket1 = serverSocket.accept();

                System.out.println("Jogador número 1 conectou:"  + socket1.getRemoteSocketAddress());
                System.out.println("Aguardando segundo jogador");

                Socket socket2 = serverSocket.accept();

                System.out.println("Jogador número 2 conectou:"  + socket2.getRemoteSocketAddress());
                System.out.println("Começando o jogo!");

                while (true) {

                    scanner = new Scanner(socket1.getInputStream());

                    valorJogadorUm = scanner.nextLine();

                    scanner = new Scanner(socket2.getInputStream());

                    valorJogadorDois = scanner.nextLine();

                }

            } catch (Exception e) {
                System.out.println("Erro.");
                return;
            }
        } else {

        }


    }
}
