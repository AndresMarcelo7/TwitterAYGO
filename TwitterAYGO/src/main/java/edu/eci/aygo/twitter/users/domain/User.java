package edu.eci.aygo.twitter.users.domain;

import com.fasterxml.jackson.annotation.JsonAlias;

public class User {
    @JsonAlias("username")
    public String UserName;
    @JsonAlias("email")
    public String Email;

    public User() {
    }

    public User(String userName, String email) {
        UserName = userName;
        Email = email;
    }

}
