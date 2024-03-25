package org.example;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;                      //socket declaration.
    private BufferedReader bufferedReader;      //bufferReader declaration.
    private BufferedWriter bufferedWriter;

    public void connect(String host, int port) throws IOException {
        socket = new Socket(host, port);
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void disconnect() throws IOException{
        socket.close();
        bufferedReader.close();
        bufferedWriter.close();
    }
}