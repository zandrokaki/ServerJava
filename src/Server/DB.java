
package sockets.server;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Random;
import sockets.server.RegisterException;
public class DB {
    private HashMap<String, User> userMap;
    public DB(){
        userMap = new HashMap<>();
    }
    public HashMap<String, User> getUsers(){
        return userMap;
    }
    public void registerUser(String name, String password) throws RegisterException{
        if(isRegistered(name))
            throw new RegisterException("E: This user already exists");
        userMap.put(name, new User(name, password));
    }
    public String logIn(String name, String password) throws RegisterException, NoSuchAlgorithmException{
        if(!isRegistered(name))
            throw new RegisterException("E: This user doesn't exist");
        if(!userMap.get(name).getPassword().equals(password))
            throw new RegisterException("E: Password incorrect");
        String encoded = generateToken();
        userMap.get(name).setToken(encoded);
        return encoded;
    }
    public boolean isRegistered(String name){
        return userMap.containsKey(name);
    }
    private String generateToken() throws NoSuchAlgorithmException{
        Random rand = new Random();
        int r1 = rand.nextInt(1000000);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(Integer.toString(r1).getBytes(StandardCharsets.UTF_8));
        String encoded = Base64.getEncoder().encodeToString(hash);
        return encoded;
    }
    public void checkToken(String token, String name)throws RegisterException, TokenException{
        if(!isRegistered(name))
            throw new RegisterException("E: This user doesn't exist");
        if(!userMap.get(name).getToken().equals(token))
            throw new TokenException("E: The token doesn´t match user name");
    }
}