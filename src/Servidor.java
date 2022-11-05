import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

class Servidor {
    public static final int PORT = 9863;
    private ServerSocket serverSocket;
    private List<ClienteObject> clientes = new LinkedList<>();

    public void start() throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println("Servidor iniciando na porta " + PORT);
        serverloop();
    }

    private void serverloop() throws IOException {
        Scanner scanner;

        while (true){
            ClienteObject clienteObject = new ClienteObject(serverSocket.accept());

            clientes.add(clienteObject);

            new Thread(() -> clienteLoop(clienteObject)).start();
        }
    }

    public void clienteLoop(ClienteObject clienteObject){
        String msg;
        try {
            while ((msg = clienteObject.getMessage()) != null) {
                if ("sair".equalsIgnoreCase(msg))
                    return;
                System.out.println("Cliente: " + clienteObject.getRemoteSocketAddress() + "enviou a mensagem: " + msg);
            }
        } finally {
            clienteObject.close();
        }
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