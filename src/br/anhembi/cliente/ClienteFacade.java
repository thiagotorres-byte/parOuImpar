package br.anhembi.cliente;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;

public class ClienteFacade {
    private Socket socket;
    private Scanner inputMessage;
    private PrintStream outputMessage;

    public ClienteFacade(Socket socket) throws IOException {
        this.socket = socket;

        System.out.println("br.anhembi.cliente.Cliente conectado " + socket.getRemoteSocketAddress());

        inputMessage = new Scanner(socket.getInputStream());

        outputMessage = new PrintStream(socket.getOutputStream());
    }

    public void close(){
        try{
            inputMessage.close();
            outputMessage.close();
            socket.close();
        } catch (IOException ex) {
            System.out.println("Erro ao fechar socket");
        }

    }

    public SocketAddress getRemoteSocketAddress(){
        return socket.getRemoteSocketAddress();
    }

    public String getMessage() {
        return inputMessage.nextLine();
    }

    public boolean sendMessage(String msg) {
        outputMessage.println(msg);
        return !outputMessage.checkError();
    }

}
