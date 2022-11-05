import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

class Cliente implements Runnable {
    private final String SERVER_ADRESS = "127.0.0.1";
    private Scanner scanner;
    ClienteObject clienteObject;

    public Cliente() throws IOException {
        clienteObject = new ClienteObject(
                new Socket(SERVER_ADRESS, Servidor.PORT)
        );
        this.scanner = new Scanner(System.in);
    }

    public void start() throws IOException {
        try {
            Socket clienteSocket = new Socket(SERVER_ADRESS, Servidor.PORT);

            new Thread(this).start();

            enviarMensagemLoop();
        } finally {
            clienteObject.close();
        }
    }

    private void enviarMensagemLoop() {
        String msg;
        do {
            System.out.println("Digite uma mensagem: ");
            msg = scanner.nextLine();
            clienteObject.sendMessage(msg);
            System.out.println("Mensagem recebida do servidor: " + clienteObject.getMessage());
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
                System.out.println("Mensagem servidor: " + msg);
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

    public Cliente2() throws IOException {
        clienteObject = new ClienteObject(
                new Socket(SERVER_ADRESS, Servidor.PORT)
        );
        this.scanner = new Scanner(System.in);
    }

    public void start() throws IOException {
        try {
            Socket clienteSocket = new Socket(SERVER_ADRESS, Servidor.PORT);

            new Thread(this).start();

            enviarMensagemLoop();
        } finally {
            clienteObject.close();
        }
    }

    private void enviarMensagemLoop() {
        String msg;
        do {
            System.out.println("Digite uma mensagem: ");
            msg = scanner.nextLine();
            clienteObject.sendMessage(msg);
            System.out.println("Mensagem recebida do servidor: " + clienteObject.getMessage());
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
                System.out.println("Mensagem servidor: " + msg);
            }
        } finally {
            clienteObject.close();
        }
    }
}