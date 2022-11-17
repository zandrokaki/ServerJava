package sockets.server;

import java.io.*;
import java.util.logging.Logger;
import java.net.Socket;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

public class ThreadServer extends Thread {

    private Socket socket;

    private ArrayList<Socket> clients;
    private HashMap<Socket, String> clientNameList;
    private boolean status = false;
    private DB db;
    private Logger logger;

    public ThreadServer(Socket socket, ArrayList<Socket> clients, HashMap<Socket, String> clientNameList, DB db) {
        this.socket = socket;
        this.clients = clients;
        this.clientNameList = clientNameList;
        this.db = db;
        this.logger = Logger.getLogger("Server");
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String outputString = input.readLine();
                //LOGOUT
                System.out.println(outputString);

                if (outputString.contains("out-111117116")) {
                    status=false;
                    
                }
                //REG
                if ((outputString.contains("reg-114101103"))) {

                    String user = outputString.split("reg-114101103")[1].split("115101112097114097100111114")[0];

                    String password = outputString.split("reg-114101103")[1].split("115101112097114097100111114")[1];

                    try {
                        db.registerUser(user, password);
                        //TODO GASPAR DICE QUE NO MANDA MENSAJE
                        sendMessageToSender(socket, "reg-114101103" + ":" + "successfull");
                        logger.log(Level.INFO, "" + user + " Has been registered succesfully");
                    } catch (RegisterException e) {

                        sendMessageToSender(socket, "reg-114101103" + e.getMessage());
                    }
                }
                //LOGIN
                if ((outputString.contains("log-108111103"))) {
                    String user = outputString.split("log-108111103")[1].split("115101112097114097100111114")[0];
                    String password = outputString.split("log-108111103")[1].split("115101112097114097100111114")[1];
                    try {

                        sendMessageToSender(socket, "log-108111103" + ":" + db.logIn(user, password));
                        status = true;
                        logger.log(Level.INFO, "" + user + " Has been logged succesfully");

                    } catch (IOException e) {
                        sendMessageToSender(socket, "log-108111103" + e.getMessage());

                    } catch (RegisterException | NoSuchAlgorithmException re) {
                        sendMessageToSender(socket, re.getMessage());
                    }

                }
                //TOKEN

                if ((outputString.contains("tok-116111107"))) {
                    String token = outputString.split("tok-116111107")[1].split("usr-117115114")[0];
                    String user = outputString.split("usr-117115114")[1].split("mes-109101115")[0];
                    String message = outputString.split("mes-109101115")[1];
                    try {
                        db.checkToken(token, user);
                        if (!clientNameList.containsKey(socket)) {
                            clientNameList.put(socket, user);
                        }
                        System.out.println(user + ": " + message);
                        showMessageToAllClients(socket, user + ":" + message);
                    } catch (TokenException | RegisterException e) {
                        sendMessageToSender(socket, "tok-116111107" + e.getMessage());
                    }

                }
            }
        } catch (SocketException e) {
            String printMessage = clientNameList.get(socket) + " left the chat room";
            System.out.println(printMessage);
            showMessageToAllClients(socket, printMessage);
            clients.remove(socket);
            clientNameList.remove(socket);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("E: It has not been possible to encrypt the message");
        } catch (IOException e1) {
            System.out.println("E: It has not been possible to read the line");
        }

    }

    private void sendMessageToSender(Socket sender, String outputString) throws IOException, NoSuchAlgorithmException {
        PrintWriter printWriter = new PrintWriter(sender.getOutputStream(), true);
        if (printWriter == null) {
            throw new IOException("E: Message not found");

        }
        printWriter.println(outputString);

    }

    private void showMessageToAllClients(Socket sender, String outputString) {
        
        PrintWriter printWriter;
        int i = 0;
        while (i < clients.size()) {
            socket = clients.get(i);
            i++;
            try {
                if (socket != sender && status == true) {
                    printWriter = new PrintWriter(socket.getOutputStream(), true);
                    printWriter.println(outputString);
                }
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
    }

}
