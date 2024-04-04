package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private final ServerSocket serverSocket;
    private final ArrayList<Thread> clients;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.clients = new ArrayList<>();
    }

    public static void main(String[] args) {
        try{
            Server server = new Server(new ServerSocket(8000));
            server.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void start() throws IOException {
        while (!serverSocket.isClosed()){
            Socket socket = serverSocket.accept();                  //program will hold here until the client connects to us.
            System.out.println("A new client has connected!");
            Thread thread = new Thread(() -> handleClient(socket));                  //client gets its own thread.
            clients.add(thread);
            thread.start();
        }
    }

    private void handleClient(Socket socket){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));  //TODO

            while (socket.isConnected()){
                String message = in.readLine();
                System.out.println("Client: " + message);
            }
        }catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}