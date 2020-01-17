package com.company;

public class User {

    private String name;
    private String password;
    private String userRole;

    public User(String name, String password, String userRole){
        this.name = name;
        this.password = password;
        this.userRole = userRole;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getUserRole() {
        return userRole;
    }

}
