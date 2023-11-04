package edu.eci.aygo.twitter.users.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.eci.aygo.twitter.users.domain.User;
import edu.eci.aygo.twitter.users.domain.UserCreds;

import java.util.ArrayList;

public interface UserTwitterServices {

    public User getUser(String username) throws Exception;

    public ArrayList<User> getUsers() throws Exception;

    public User createUser(UserCreds user) throws Exception;

    public String deleteUser(String username, String loggedUser) throws Exception;


}
