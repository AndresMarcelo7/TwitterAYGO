package edu.eci.aygo.twitter.users.repository.impl;

import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.aygo.twitter.core.domain.Tweet;
import edu.eci.aygo.twitter.exceptions.ResourceNotFoundException;
import edu.eci.aygo.twitter.infrastructure.clients.DynamoDbClient;
import edu.eci.aygo.twitter.users.domain.User;
import edu.eci.aygo.twitter.users.domain.UserCreds;
import edu.eci.aygo.twitter.users.repository.UserRepository;
import edu.eci.aygo.twitter.utils.ObjectMapperSingleton;

import java.util.ArrayList;

public class DynamoUserRepository implements UserRepository {

    private Table dynamoUsersTable;
    private Table dynamoUserCredsTable;
    private ObjectMapper mapper = ObjectMapperSingleton.getMapper();
    public DynamoUserRepository() {
        super();
        DynamoDB dynamoClient = DynamoDbClient.getClient();
        this.dynamoUsersTable = dynamoClient.getTable("Users");
        this.dynamoUserCredsTable = dynamoClient.getTable("TwitterUserCreds");
    }

    @Override
    public User getUser(String username) throws Exception {
        try {
            QuerySpec qs = new QuerySpec()
                    .withKeyConditionExpression("username = :v_id")
                    .withValueMap(new ValueMap().withString(":v_id", username));
            ItemCollection<QueryOutcome> result = dynamoUsersTable.query(qs);
            ArrayList<User> users = new ArrayList<>();
            for (Item item : result) {
                User u = mapper.readValue(item.toJSONPretty(), User.class);
                return u;
            }
            throw new ResourceNotFoundException("User not found");
        }
        catch (Exception e){
            throw e;
        }
    }

    @Override
    public ArrayList<User> getUsers() {
        //scan the table and return all the users
        try {
            ItemCollection<ScanOutcome> result = dynamoUsersTable.scan();
            ArrayList<User> users = new ArrayList<>();
            for (Item item : result) {
                User u = mapper.readValue(item.toJSONPretty(), User.class);
                users.add(u);
            }
            return users;
        }
        catch (Exception e){
            System.out.println("Something went wrong with DynamoDB call");
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public User createUser(UserCreds userCreds) {
        // insert the user info in users table and username and password in the other
        try {
            dynamoUsersTable.putItem(
                    new com.amazonaws.services.dynamodbv2.document.Item()
                            .withPrimaryKey("username", userCreds.Username)
                            .withString("email", userCreds.Email));
            dynamoUserCredsTable.putItem(
                    new Item().withPrimaryKey("Username",userCreds.Username)
                            .withString("Password", userCreds.Password)
            );
            return new User(userCreds.Username, userCreds.Email);
        }
        catch (Exception e){
            System.out.println("Something went wrong with DynamoDB call");
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @Override
    public String deleteUser(String username) {
        try {
            DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                    .withPrimaryKey("username", username);

            DeleteItemOutcome outcome = dynamoUsersTable.deleteItem(deleteItemSpec);
            DeleteItemSpec deleteItemSpec2 = new DeleteItemSpec()
                    .withPrimaryKey("Username", username);

            DeleteItemOutcome outcome2 = dynamoUserCredsTable.deleteItem(deleteItemSpec2);
            return "User deleted";
        }
        catch (Exception e){
            System.out.println("Something went wrong with DynamoDB call");
            System.out.println(e.getMessage());
            return "User not found";
        }
    }
}
