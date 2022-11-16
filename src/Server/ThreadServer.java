package Server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
public class ThreadServer extends Thread {
    private Socket socket;
    private ArrayList<Socket> clients;
    private HashMap<Socket, String> clientNameList;
    private DataBase db;
    public ThreadServer(Socket socket, ArrayList<Socket> clients, HashMap<Socket, String> clientNameList, DataBase db) {
        this.socket = socket;
        this.clients = clients;
        this.clientNameList = clientNameList;
        this.db = db;
    }
    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String outputString = input.readLine();
                if (outputString.equals("logout")) {
                    throw new SocketException();
                }
                if((outputString.contains("reg-114101103"))){
                    String user = outputString.split("reg-114101103")[1].split("115101112097114097100111114")[0];
                    String password = outputString.split("reg-114101103")[1].split("115101112097114097100111114")[1];
                    try{
                        db.registerUser(user, password);
                        sendMessageToSender(socket, "reg-114101103"+"successfull");
                    }catch(Exception e){
                        sendMessageToSender(socket, "reg-114101103"+e.getMessage());
                    }
                }
                if((outputString.contains("log-108111103"))){
                    String user = outputString.split("log-108111103")[1].split("115101112097114097100111114")[0];
                    String password = outputString.split("log-108111103")[1].split("115101112097114097100111114")[1];
                    try{
                        db.registerUser(user, password);
                    }catch(Exception e){
                    }
                }
                if((outputString.startsWith("tok-116111107"))){
                }
                if (!clientNameList.containsKey(socket)) {
                    String[] messageString = outputString.split(":", 2);
                    clientNameList.put(socket, messageString[0]);
                    System.out.println(messageString[0] + messageString[1]);
                    showMessageToAllClients(socket, messageString[0] + messageString[1]);
                } else {
                    System.out.println(outputString);
                    showMessageToAllClients(socket, outputString);
                }
            }
        } catch (SocketException e) {
            String printMessage = clientNameList.get(socket) + " left the chat room";
            System.out.println(printMessage);
            showMessageToAllClients(socket, printMessage);
            clients.remove(socket);
            clientNameList.remove(socket);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }
    private void sendMessageToSender(Socket sender, String outputString){
        Socket socket;
        PrintWriter printWriter;
        int i = 0;
        while (i < clients.size()) {
            socket = clients.get(i);
            i++;
            try {
                if (socket == sender) {
                    printWriter = new PrintWriter(socket.getOutputStream(), true);
                    printWriter.println(outputString);
                }
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
    }
    private void showMessageToAllClients(Socket sender, String outputString) {
        Socket socket;
        PrintWriter printWriter;
        int i = 0;
        while (i < clients.size()) {
            socket = clients.get(i);
            i++;
            try {
                if (socket != sender) {
                    printWriter = new PrintWriter(socket.getOutputStream(), true);
                    printWriter.println(outputString);
                }
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
    }
}
