import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

class Cliente {
    private Socket clienteSocket;
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
        clienteSocket = new Socket(SERVER_ADRESS, Servidor.PORT);

        enviarMensagemLoop();
    }

    private void enviarMensagemLoop() {
        String msg;
        do {
            System.out.println("Digite uma mensagem: ");
            msg = scanner.nextLine();
            clienteObject.sendMessage(msg);
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
}