package edu.eci.aygo.twitter.core.repository.impl;

import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.aygo.twitter.core.domain.Tweet;
import edu.eci.aygo.twitter.exceptions.ResourceNotFoundException;
import edu.eci.aygo.twitter.core.repository.TweetRepository;
import edu.eci.aygo.twitter.infrastructure.clients.DynamoDbClient;
import edu.eci.aygo.twitter.utils.ObjectMapperSingleton;

import java.util.ArrayList;
import java.util.List;

public class DynamoTweetRepository implements TweetRepository {

    private Table dynamoTweetTable;
    private ObjectMapper mapper = ObjectMapperSingleton.getMapper();


    public DynamoTweetRepository() {
        super();
        DynamoDB dynamoClient = DynamoDbClient.getClient();
        this.dynamoTweetTable = dynamoClient.getTable("Tweets");
    }

    @Override
    public void SaveTweet(Tweet t) {
        try {
            dynamoTweetTable.putItem(
                    new com.amazonaws.services.dynamodbv2.document.Item()
                            .withPrimaryKey("tweetId", t.TweetId)
                            .withString("message", t.Message)
                            .withString("mediaUrl", t.MediaUrl)
                            .withString("date", t.Date.toString())
                            .withInt("likes", t.Likes)
                            .withString("authorId", t.AuthorId));

        }
        catch (Exception e){
            System.out.println("Something went wrong with DynamoDB call");
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void DeleteTweet(Long tweetId) throws Exception {
        try {
            Tweet t = GetTweetById(tweetId);
            if(t == null) throw new Exception("Tweet not found");
            DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                    .withPrimaryKey("tweetId", tweetId);

            DeleteItemOutcome outcome = dynamoTweetTable.deleteItem(deleteItemSpec);
        }
        catch (Exception e){
            throw e;
        }
    }

    @Override
    public void UpdateTweet(Tweet t) throws Exception{
        try {
            UpdateItemSpec updateItemSpec = new UpdateItemSpec()
                    .withPrimaryKey("tweetId", t.TweetId)
                    .withUpdateExpression("set #message = :message, #mediaUrl=:mediaUrl, #date=:date")
                    .withNameMap(new NameMap().with( "#message", "message").with("#mediaUrl", "mediaUrl").with("#date", "date"))
                    .withValueMap(new ValueMap().withString(":message", t.Message).withString(":mediaUrl", t.MediaUrl).withString(":date", t.Date.toString()));

            UpdateItemOutcome outcome = dynamoTweetTable.updateItem(updateItemSpec);
            System.out.println("Update Item Result: " + outcome.toString());
        }
        catch (Exception e){
            throw e;
        }

    }

    @Override
    public Tweet GetTweetById(Long Id) throws Exception {
        try {
            QuerySpec qs = new QuerySpec()
                    .withKeyConditionExpression("tweetId = :v_id")
                    .withValueMap(new ValueMap().withNumber(":v_id", Id));
            ItemCollection<QueryOutcome> result = dynamoTweetTable.query(qs);

            for (Item item : result) {
                Tweet tweet = mapper.readValue(item.toJSONPretty(), Tweet.class);
                return tweet;
            }

            throw new ResourceNotFoundException("Tweet not found");

        }
        catch (Exception e){
            throw e;
        }
    }

    @Override
    public List<Tweet> GetTweetsByUser(String authorId) throws JsonProcessingException {
        try {
            QuerySpec qs = new QuerySpec()
                    .withKeyConditionExpression("authorId = :v_id")
                    .withValueMap(new ValueMap().withString(":v_id", authorId));
            ItemCollection<QueryOutcome> result = dynamoTweetTable.query(qs);
            ArrayList<Tweet> tweets = new ArrayList<>();
            for (Item item : result) {
                Tweet tweet = mapper.readValue(item.toJSONPretty(), Tweet.class);
                tweets.add(tweet);
            }

            return tweets;

        }
        catch (Exception e){
            throw e;
        }
    }

    @Override
    public List<Tweet> GetAllTweets() throws JsonProcessingException {
        try {
            // get all the tweets by an user (user is order key)
            ItemCollection<ScanOutcome> result = dynamoTweetTable.scan();
            ArrayList<Tweet> tweets = new ArrayList<>();
            for (Item item: result) {
                Tweet tweet = mapper.readValue(item.toJSONPretty(), Tweet.class);
                tweets.add(tweet);
            }
            return tweets;
        }
        catch (Exception e){
            throw e;
        }
    }
}
