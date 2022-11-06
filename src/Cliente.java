import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

class Cliente implements Runnable {
    private final String SERVER_ADRESS = "127.0.0.1";
    ClienteObject clienteObject;
    private Scanner scanner;
    String msg;
    List<String> numerosPermitidos = Arrays.asList("0", "1", "2", "3", "4", "5");

    public Cliente() throws IOException {
        this.scanner = new Scanner(System.in);
    }

    public void start() throws IOException {
        try {
            clienteObject = new ClienteObject(
                    new Socket(SERVER_ADRESS, Servidor.PORT)
            );

            new Thread(this).start();

            do {
                PainelJogo.imprimir();

                msg = scanner.nextLine();

                if ("1".equalsIgnoreCase(msg)) {
                    enviarMensagemLoopVsPlayer();
                } else if ("2".equalsIgnoreCase(msg)) {
                    enviarMensagemLoopVsCPU();
                }

            } while (!msg.equalsIgnoreCase("3"));

        } finally {
            clienteObject.close();
        }
    }

    private void enviarMensagemLoopVsCPU() {

        clienteObject.sendMessage(msg);

        msg = scanner.nextLine();

        clienteObject.sendMessage(msg);

        do {

            msg = scanner.nextLine();

            if (numerosPermitidos.contains(msg))
                clienteObject.sendMessage(msg);
            else {
                System.out.println("Voce não escolheu um numero de 0 a 5... Tente novamente! ");
            }
        } while(!msg.equalsIgnoreCase("sair"));
    }

    private void enviarMensagemLoopVsPlayer() {
        msg = scanner.nextLine();

        clienteObject.sendMessage(msg);

        do {
            msg = scanner.nextLine();
            if (numerosPermitidos.contains(msg))
                clienteObject.sendMessage(msg);
            else {
                System.out.println("Você não escolheu um numero de 0 a 5... Tente novamente! ");
            }
        } while(!msg.equalsIgnoreCase("sair"));
    }

    public static void main(String[] args) {
        try {
            Cliente cliente = new Cliente();
            cliente.start();
        } catch (IOException ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
    }

    @Override
    public void run() {
        String msg;
        try {
            while ((msg = clienteObject.getMessage()) != null) {
                if ("sair".equalsIgnoreCase(msg))
                    return;
                System.out.println(msg);
            }
        } finally {
            clienteObject.close();
        }
    }
}

class Cliente2 implements Runnable {
    private final String SERVER_ADRESS = "127.0.0.1";
    private Scanner scanner;
    ClienteObject clienteObject;
    List<Integer> numerosPermitidos = Arrays.asList(0, 1, 2, 3, 4, 5);

    public Cliente2() throws IOException {
        this.scanner = new Scanner(System.in);
    }

    public void start() throws IOException {
        try {
            clienteObject = new ClienteObject(
                    new Socket(SERVER_ADRESS, Servidor.PORT)
            );

            new Thread(this).start();

            enviarMensagemLoop();
        } finally {
            clienteObject.close();
        }
    }

    private void enviarMensagemLoop() {
        String msg;
        do {
            msg = scanner.nextLine();
            clienteObject.sendMessage(msg);
        } while(!msg.equalsIgnoreCase("sair"));
    }

    public static void main(String[] args) {
        try {
            Cliente2 cliente = new Cliente2();
            cliente.start();
        } catch (IOException ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
    }

    @Override
    public void run() {
        String msg;
        try {
            while ((msg = clienteObject.getMessage()) != null) {
                if ("sair".equalsIgnoreCase(msg))
                    return;
                System.out.println(msg);
            }
        } finally {
            clienteObject.close();
        }
    }
}