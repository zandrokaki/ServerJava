package Server;

import java.util.HashMap;

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

        
        return "";
        
    }

    public boolean isRegistered(String name){
        return userMap.containsKey(name);
    }
}
