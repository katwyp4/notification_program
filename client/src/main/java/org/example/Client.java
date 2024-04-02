package org.example;

import java.io.*;
import java.net.Socket;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    private Socket socket;                      //socket declaration.
    private BufferedReader bufferedReader;      //bufferReader declaration.
    private BufferedWriter bufferedWriter;

    private ArrayList<String> messages;

    public static void main(String[] args) {
        Client client = new Client();
        try{
            client.connect("localhost", 8000);
            client.receive();
            client.queue();
            Scanner scanner = new Scanner(System.in);
            while(client.getSocket().isConnected()){
                System.out.println("Podaj tekst:");
                String answer = scanner.nextLine();
                System.out.println("Podaj czas:");
                String time = scanner.nextLine();
                client.getMessages().add(answer + "," + time);
            }
        }catch (Exception e){
            client.disconnect();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public void connect(String host, int port) throws IOException {
        socket = new Socket(host, port);
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        messages = new ArrayList<>();
        System.out.println("Server connected: " + host + ":" + port);
    }

    public void disconnect(){
        try{
            if (socket != null) {
                socket.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
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
    private void send(String notification) throws IOException{
        bufferedWriter.write(notification);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    private void queue(){
        new Thread(() ->{
            while (!socket.isClosed()){
                try{
                    for (String message : messages) {
                        String[] parts = message.split(",");
                        if (parts.length == 2) {
                            LocalTime targetTime = LocalTime.parse(parts[1]);
                            LocalTime now = LocalTime.now();
                            Duration duration = Duration.between(now, targetTime);

                            // Sprawdzanie, czy czas docelowy jest "teraz" lub już minął
                            if (!duration.isNegative() && !duration.isZero()) {
                                // Czas docelowy jest w przyszłości, kontynuuj przeszukiwanie
                                continue;
                            }
                            send(parts[0]);
                        }
                    }
                }catch (IOException e){
                    disconnect();
                }
            }
        }).start();
    }
}