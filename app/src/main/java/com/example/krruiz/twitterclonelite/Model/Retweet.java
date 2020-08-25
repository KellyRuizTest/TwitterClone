package com.example.krruiz.twitterclonelite.Model;

public class Retweet {

    private String idUser;
    private String idTweet;

    public Retweet(){

    }

    public Retweet(String idUser, String idTweet) {
        this.idUser = idUser;
        this.idTweet = idTweet;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdTweet() {
        return idTweet;
    }

    public void setIdTweet(String idTweet) {
        this.idTweet = idTweet;
    }
}
