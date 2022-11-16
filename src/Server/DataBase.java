package Server;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Random;

public class DataBase {
    private HashMap<String, User> userMap;

    public DataBase(){
        userMap = new HashMap<>();
    }

    public HashMap<String, User> getUsers(){
        return userMap;
    }

    public void registerUser(String name, String password) throws Exception{

        if(isRegistered(name))
            throw new Exception("This user already exists");
            
        userMap.put(name, new User(name, password));
    }

    public String logIn(String name, String password) throws Exception{

        if(!isRegistered(name))
            throw new Exception("This user doesn't exist");

        Random rand = new Random();
        int r1 = rand.nextInt(1000000);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(Integer.toString(r1).getBytes(StandardCharsets.UTF_8));
        String encoded = Base64.getEncoder().encodeToString(hash);

        return encoded;
        
    }

    public boolean isRegistered(String name){
        return userMap.containsKey(name);
    }
}
