package edu.eci.aygo.twitter.users.domain;

import com.fasterxml.jackson.annotation.JsonAlias;

public class UserCreds {
    @JsonAlias("userName")
    public String Username;
    @JsonAlias("password")
    public String Password;

    @JsonAlias("email")
    public String Email;

    public UserCreds() {
    }

    public UserCreds(String username, String password, String email) {
        Username = username;
        Password = password;
        Email = email;
    }
}
