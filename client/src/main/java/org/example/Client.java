package org.example;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;                      //socket declaration.
    private BufferedReader bufferedReader;      //bufferReader declaration.
    private BufferedWriter bufferedWriter;

    public static void main(String[] args) {
        try{
            Client client = new Client();
            client.connect("localhost", 6969);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void connect(String host, int port) throws IOException {
        socket = new Socket(host, port);
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        System.out.println("Server connected: " + host + ":" + port);
    }

    public void disconnect() throws IOException{
        socket.close();
        bufferedReader.close();
        bufferedWriter.close();
    }
    public void reciveMessage() throws IOException{
        new Thread(() ->{
            while (!socket.isClosed()){
                try {
                    String message = bufferedReader.readLine();
                    if (message != null){
                        System.out.println("Message: " +message);
                    }else {
                        break;
                    }
                } catch (IOException e) {
                    System.out.println("Error receiving message");
                    try {
                        disconnect();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
                }
            }
        }).start();
    }
    private void send(Notification notification) throws IOException{
        String notificationMessage = notification.getText() + "|" + notification.getTime();
        bufferedWriter.write(notificationMessage);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }
}