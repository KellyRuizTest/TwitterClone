package com.example.krruiz.twitterclonelite.Model;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Tweet {

    private String tweet;
    private String user;
    private String name;
    private String date;
    private String time;
    private String id;
    private String imageUser;
    private String imageTweet;
    private ArrayList<String> likes;
    private ArrayList<Tweet> comments;


    public Tweet(){

        tweet = "#ThisTweetIsEmpty";
        user = "#NoUser";
        name = "#empty";
        id = "#nopid";
        likes = new ArrayList<>();
        comments = new ArrayList<>();

    }

    public Tweet (String tweet, String user, String name){
        this.tweet = tweet;
        this.user = "@"+user;
        this.name = name;
    }

    public Tweet (String tweet, String user, String name, String date, String time, String idUser){

        this.tweet = tweet;
        this.user = user;
        this.name = name;
        this.date = date;
        this.time = time;
        this.id = idUser;

    }

    public Tweet (String tweet, String user, String name, String date, String time, String idUser, String Imagen){

        this.tweet = tweet;
        this.user = user;
        this.name = name;
        this.date = date;
        this.time = time;
        this.id = idUser;

        if (Imagen.isEmpty()){
            this.imageUser = null;
        }else{
            this.imageUser = Imagen;
        }
    }
    public Tweet (String tweet, String user, String name, String date, String time, String idUser, String ImagenUser, String ImagenTweet){

        this.tweet = tweet;
        this.user = user;
        this.name = name;
        this.date = date;
        this.time = time;
        this.id = idUser;

        if (ImagenUser.isEmpty()){
            this.imageUser = null;
        }else{
            this.imageUser = ImagenUser;
        }

        this.imageTweet = ImagenTweet;
    }

    public String getImageTweet(){
        return imageTweet;
    }

    public String getId(){
        return id;
    }

    public String getImageUser(){
        return imageUser;
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

    public String getDate(){ return date; }
    public String getTime(){ return time; }

    public boolean setALike(String id){

        if (searchId(id)) {
            System.out.println("Found: "+id);
            return false;
        }else{
            likes.add(id);
            return true;
        }

    }

    public int countLikes(){ return likes.size(); }

    private boolean searchId(String id){

        for (int i=0; i<likes.size(); i++){

            if (likes.get(i) == id){
                return true;
            }
        }
        return false;
    }

}
