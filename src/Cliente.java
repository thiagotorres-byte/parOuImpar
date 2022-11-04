import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

class Cliente {
    public static void main(String[] args) {
        final String IP = "127.0.0.1";
        final int PORT = 9876;
        String msg;

        try {

            Socket socket = new Socket(IP, PORT);

            Scanner scanner = new Scanner(System.in);

            OutputStream out = socket.getOutputStream();

            PrintStream printStream = new PrintStream(out);

            System.out.println("Comecando modo Jogador vs Jogador");

            System.out.println("Escolha par ou ímpar:");

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