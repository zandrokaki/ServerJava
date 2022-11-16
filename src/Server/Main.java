package Server;


import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) throws Exception {
        Server server = new Server();
        Logger logger = Logger.getLogger(Server.class.getName());
        
        server.run();
      
        

         /*DataBase db = new DataBase();
        db.registerUser("sandro", "123");
        System.out.println( db.logIn("sandro", "123"));
       

       try{
           
        }catch(Exception e){
            logger.log(Level.INFO, e.getMessage());
        }

        logger.log(Level.INFO, db.getUsers().toString());*/

        
    }
}
