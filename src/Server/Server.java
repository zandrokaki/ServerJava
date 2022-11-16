package Server;


import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {
    private ArrayList<Socket> clients;
    private HashMap<Socket, String> clientNameList;

    public Server(){
        clients = new ArrayList<>();
        clientNameList = new HashMap<Socket, String>();
    }

    public void run(){
        try (ServerSocket serversocket = new ServerSocket(8085)) {
            System.out.println("Server is started...");
            while (true) {
                Socket socket = serversocket.accept();
                clients.add(socket);
                ThreadServer ThreadServer = new ThreadServer(socket, clients, clientNameList);
                ThreadServer.start();
            }
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }


}
