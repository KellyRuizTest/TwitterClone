package com.example.krruiz.twitterclonelite.Model;

public class Tweet {

    public String tweet;
    public String user;
    public String name;

    public Tweet(){

        tweet = "#ThisTweetIsEmpty";
        user = "#NoUser";
        name = "#empty";
    }

    public Tweet (String tweet, String user, String name){
        this.tweet = tweet;
        this.user = "@"+user;
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }


}
