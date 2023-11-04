package edu.eci.aygo.twitter.users.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.eci.aygo.twitter.users.domain.User;
import edu.eci.aygo.twitter.users.domain.UserCreds;

import java.util.ArrayList;

public interface UserRepository {

    public User getUser(String username) throws JsonProcessingException, Exception;

    public ArrayList<User> getUsers();

    public User createUser(UserCreds userCreds);

    public String deleteUser(String username);

}
