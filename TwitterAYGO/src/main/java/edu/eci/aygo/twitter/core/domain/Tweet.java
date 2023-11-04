package edu.eci.aygo.twitter.core.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.eci.aygo.twitter.users.domain.User;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Tweet {
    @JsonAlias("message")
    public String Message;
    @JsonAlias("mediaUrl")
    public String MediaUrl;
    @JsonAlias("tweetId")
    public Long TweetId;
    @JsonAlias("authorId")
    public String AuthorId;
    @JsonAlias("likes")
    public Integer Likes;
    @JsonAlias("date")
    public DateTime Date;

    public Tweet() {
    }
    public Tweet(String message, String mediaUrl, String authorId) {
        Message = message;
        MediaUrl = mediaUrl;
        //UUID OF lenght 7
        TweetId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        AuthorId = authorId;
        Likes = 0;
        Date = DateTime.now().toDateTime();
    }

    public Tweet(String message, String mediaUrl, Long tweetId, Integer likes, DateTime date, String authorId) {
        Message = message;
        MediaUrl = mediaUrl;
        TweetId = tweetId;
        Likes = likes;
        Date = date;
        AuthorId = authorId;
    }

    public void LikeTweet(){
        Likes++;
    }
    public Integer GetLikeCount(){
        return Likes;
    }


    public void FillData() {
        //UUID OF lenght 7
        TweetId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        Date = DateTime.now().toDateTime();
        Likes = 0;
    }
}
