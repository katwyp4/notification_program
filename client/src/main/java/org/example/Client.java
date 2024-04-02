package org.example;

import java.io.*;
import java.net.Socket;
import java.time.Duration;
import java.time.LocalTime;

public class Client {
    private Socket socket;                      //socket declaration.
    private BufferedReader bufferedReader;      //bufferReader declaration.
    private BufferedWriter bufferedWriter;

    public static void main(String[] args) {
        Client client = new Client();
        try{
            client.connect("localhost", 8000);
            client.receive();
            while(true){

            }
        }catch (Exception e){
            client.disconnect();
        }
    }

    public void connect(String host, int port) throws IOException {
        socket = new Socket(host, port);
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        System.out.println("Server connected: " + host + ":" + port);
    }

    public void disconnect(){
        try{
            socket.close();
            bufferedReader.close();
            bufferedWriter.close();
        }catch (IOException e){
            System.out.println("Unable to close connection");
        }
    }
    public void receive(){
        new Thread(() ->{
            while (!socket.isClosed()){
                try{
                    String message = bufferedReader.readLine();
                    System.out.println("Message: " + message);
                }catch (IOException e){
                    disconnect();
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