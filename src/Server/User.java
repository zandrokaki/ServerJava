package Server;

public class User {
    private String name;
    private String password;
    private String token;

    public User(String name, String password){
        this.name = name;
        this.password = password;
        this.token = "";
    }

    public void setToken(String token){
        this.token = token;
    }

    public String getName(){
        return this.name;
    }

    public String getToken(){
        return this.token;
    }

    @Override
    public String toString(){
        return "[" + this.name + "]";
    }


}
