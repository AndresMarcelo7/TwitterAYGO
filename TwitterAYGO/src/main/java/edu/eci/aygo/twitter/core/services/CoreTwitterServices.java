package edu.eci.aygo.twitter.core.services;

import edu.eci.aygo.twitter.core.domain.Tweet;
import edu.eci.aygo.twitter.users.domain.User;

import java.util.List;

public interface CoreTwitterServices {
    public Tweet PostTweet(Tweet t);
    public void DeleteTweet(Long tweetId, String user) throws Exception;
    public void UpdateTweet(Tweet t, String user) throws Exception;
    public Tweet GetTweetById(Long Id) throws Exception;
    public List<Tweet> GetTweetsByUser(String UserId);
    public List<Tweet> GetAllTweets();

}
