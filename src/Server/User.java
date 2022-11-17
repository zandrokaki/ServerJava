
package sockets.server;


public class User {
    
    private String user;
    private String password;
    private String token;
    
    public User (String user, String password){
        this.user=user;
        this.password=password;
        token="";
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
}
