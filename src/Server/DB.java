
package Server;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Random;

import Exceptions.RegisterException;
import Exceptions.TokenException;


public class DB {
    private HashMap<String, User> userMap;

    public DB(){
        userMap = new HashMap<>();
        this.readDB();

    }

    public HashMap<String, User> getUsers(){
        return userMap;
    }

    public void registerUser(String name, String password) throws RegisterException, IOException{
        if(isRegistered(name))
            throw new RegisterException("E: This user already exists");
            
        userMap.put(name, new User(name, password));

        this.createUserInfoFile(name, password);
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
            throw new TokenException("E: The token does not match user name");
    }

    public void createUserInfoFile (String name, String password) throws IOException {
        Path p = Paths.get("./db/usersDB.txt");
        try {
            Files.createFile(p);
        }
        catch (FileAlreadyExistsException ex) {
            System.err.println("File already exists");
        }
        finally {
            String entry = name + "|" + password;
            Files.write(p, entry.getBytes(), StandardOpenOption.APPEND);
        }
    }
    
    public void readDB () {
        String data;
        String [] dataArray;
        Path p = Paths.get("./db/usersDB.txt");
        try {
            data = new String(Files.readAllBytes(p));
            dataArray = data.split("\n");
            for (int i=0;i<dataArray.length;i++){
                String [] nameAndPass = dataArray[i].split("\\|");
                this.userMap.put(nameAndPass[0], new User(nameAndPass[0], nameAndPass[1]));
            }
        } catch (IOException e) {
            System.err.println("The file can't be readed");
        }
    }
}