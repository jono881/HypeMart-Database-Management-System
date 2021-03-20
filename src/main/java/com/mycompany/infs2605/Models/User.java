package com.mycompany.infs2605;


public class User {
    private int user_ID;
    private String username;
    private String password;
    private String full_name;
    private String user_type;

    public User(int user_ID, String username, String password, String full_name, String user_type) {
        this.user_ID = user_ID;
        this.username = username;
        this.password = password;
        this.full_name = full_name;
        this.user_type = user_type;
    }

    public int getUser_ID() {
        return user_ID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_ID(int user_ID) {
        this.user_ID = user_ID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
    
            
}
