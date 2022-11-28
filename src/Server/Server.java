

package Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


public class Server {

    public static void main(String[] args) {
        ArrayList<Socket> clients = new ArrayList<>();
        HashMap<Socket, String> clientNameList = new HashMap<Socket, String>();
        DB db = new DB();

        
        try (ServerSocket serversocket = new ServerSocket(8085)) {
            System.out.println("Server is started...");
            while (true) {
                Socket socket = serversocket.accept();
                clients.add(socket);
                ThreadServer ThreadServer = new ThreadServer(socket, clients, clientNameList, db);
                ThreadServer.start();
            }
        } catch (Exception e) {
           
            System.out.println(e.getStackTrace());
        }
    }
}
