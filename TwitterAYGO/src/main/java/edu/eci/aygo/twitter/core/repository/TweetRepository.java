package edu.eci.aygo.twitter.core.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.eci.aygo.twitter.core.domain.Tweet;
import edu.eci.aygo.twitter.users.domain.User;

import java.util.List;

public interface TweetRepository {

    public void SaveTweet(Tweet t);
    public void DeleteTweet(Long tweetId) throws Exception;
    public void UpdateTweet(Tweet t)  throws Exception;
    public Tweet GetTweetById(Long Id) throws Exception;
    public List<Tweet> GetTweetsByUser(String UserId) throws JsonProcessingException;
    public List<Tweet> GetAllTweets() throws JsonProcessingException;

}
