package Server;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestServer {
    
    @Test
    public void addUser(){
        DataBase db = new DataBase();

        try{
            db.registerUser("sandro", "123");
           
        }catch(Exception e){
           System.out.println("Exception");
        }

        //assertEquals(true, true);
    }
}
