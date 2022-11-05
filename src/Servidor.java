import java.io.IOException;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
                if (msg.equalsIgnoreCase("sair"))
                    return;

                System.out.println("Cliente: " + clienteObject.getRemoteSocketAddress()+ " enviou a mensagem: " + msg);
                sendMessageToAll(clienteObject, msg);
            }
        } finally {
            clienteObject.close();
        }
    }

    private void sendMessageToAll (ClienteObject sender, String msg){
        Iterator<ClienteObject> iterator = clientes.iterator();
        while(iterator.hasNext()) {
            ClienteObject clienteObject = iterator.next();
            if (!sender.equals(clienteObject))
                if(!clienteObject.sendMessage(msg)){
                    iterator.remove();
                }
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