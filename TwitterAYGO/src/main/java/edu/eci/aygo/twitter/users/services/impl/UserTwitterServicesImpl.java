package edu.eci.aygo.twitter.users.services.impl;

import edu.eci.aygo.twitter.core.repository.TweetRepository;
import edu.eci.aygo.twitter.core.repository.impl.DynamoTweetRepository;
import edu.eci.aygo.twitter.exceptions.UnauthorizedException;
import edu.eci.aygo.twitter.users.domain.User;
import edu.eci.aygo.twitter.users.domain.UserCreds;
import edu.eci.aygo.twitter.users.repository.UserRepository;
import edu.eci.aygo.twitter.users.repository.impl.DynamoUserRepository;
import edu.eci.aygo.twitter.users.services.UserTwitterServices;

import java.util.ArrayList;

public class UserTwitterServicesImpl implements UserTwitterServices {

    private UserRepository repository = new DynamoUserRepository();
    public UserTwitterServicesImpl() {
    }

    @Override
    public User getUser(String username) throws Exception {
        try {
            return repository.getUser(username);
        }
        catch (Exception e){
            throw e;
        }
    }

    @Override
    public ArrayList<User> getUsers() throws Exception {
        try {
            return repository.getUsers();
        }
        catch (Exception e){
            throw e;
        }
    }

    @Override
    public User createUser(UserCreds user) throws Exception {
        try {
            return repository.createUser(user);
        }
        catch (Exception e){
            throw e;
        }
    }

    @Override
    public String deleteUser(String username, String loggedUser) throws Exception {
        try {
            if (loggedUser != null && !loggedUser.equals(username)){
                throw new UnauthorizedException("You can't delete this user");
            }
            User u = repository.getUser(username);
            return repository.deleteUser(u.UserName);
        }
        catch (Exception e){
            throw e;
        }
    }
}
