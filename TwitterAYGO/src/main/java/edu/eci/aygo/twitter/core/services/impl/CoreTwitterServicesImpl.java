package edu.eci.aygo.twitter.core.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.eci.aygo.twitter.core.domain.Tweet;
import edu.eci.aygo.twitter.exceptions.ResourceNotFoundException;
import edu.eci.aygo.twitter.exceptions.UnauthorizedException;
import edu.eci.aygo.twitter.core.repository.TweetRepository;
import edu.eci.aygo.twitter.core.repository.impl.DynamoTweetRepository;
import edu.eci.aygo.twitter.core.services.CoreTwitterServices;
import org.joda.time.DateTime;

import java.util.List;

public class CoreTwitterServicesImpl implements CoreTwitterServices {

    private TweetRepository repository = new DynamoTweetRepository();
    public CoreTwitterServicesImpl() {
    }

    @Override
    public Tweet PostTweet(Tweet t) {
        t.FillData();
        repository.SaveTweet(t);
        return t;
    }

    @Override
    public void DeleteTweet(Long tweetId, String author) throws Exception {
        try {
            Tweet t = repository.GetTweetById(tweetId);
            if (!t.AuthorId.equals(author)){
                throw new UnauthorizedException("You can't delete this tweet");
            }
            repository.DeleteTweet(tweetId);
        }
        catch (Exception e) {
            throw e;
        }

    }

    @Override
    public void UpdateTweet(Tweet t, String user) throws Exception {
        try {
            Tweet tweet = repository.GetTweetById(t.TweetId);
            if (tweet != null && !tweet.AuthorId.equals(user)){
                throw new UnauthorizedException("You can't update this tweet");
            }
            t.Date = DateTime.now().toDateTime();
            repository.UpdateTweet(t);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Tweet GetTweetById(Long Id) throws Exception {
        try {
            Tweet tweet = repository.GetTweetById(Id);
            if (tweet == null){
                throw new ResourceNotFoundException("Tweet not found");
            }
            return repository.GetTweetById(Id);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<Tweet> GetTweetsByUser(String UserId) {
        try {
            return repository.GetTweetsByUser(UserId);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Tweet> GetAllTweets() {
        try {
            return repository.GetAllTweets();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
