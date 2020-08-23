package com.example.krruiz.twitterclonelite.Model;

import java.util.List;

public class Users {

    private String email;
    private String name;
    private String idUser;
    private String id;
    private String password;
    private String image;
    private String banner;
    private String bio;
    private String location;
    private String sitioweb;

    //public List <Users> followers;
    //public List <Users> followings;

    public List<Tweet> tweets;

    public Users(){

        email = "NoEmail setted";
        name = "NoName";
        id = "Bitcoin Revolution";
        password = "NoPW";
        image = "#noURLimage";
        banner = "#noURLbanner";
        bio = "#noBio";
        location ="NoLocation";
        sitioweb = "www.google.com";
    }

    public Users (String email, String name, String id, String password, String imageurl, String bannerurl){

        this.email = email;
        this.name = name;
        this.idUser = id;
        this.password = password;
        this.image = imageurl;
        this.banner = bannerurl;
    }

    public Users (String email, String name, String id, String idUser, String password, String imageurl, String bannerurl){

        this.email = email;
        this.name = name;
        this.idUser = idUser;
        this.password = password;
        this.image = imageurl;
        this.banner = bannerurl;
        this.id = id;
    }
    public Users(String email, String name, String id, String idUser, String password, String imageurl, String bannerurl, String bio, String location, String sitioweb) {
        this.email = email;
        this.name = name;
        this.id = id;
        this.idUser = idUser;
        this.password = password;
        this.image = imageurl;
        this.banner = bannerurl;
        this.bio = bio;
        this.location = location;
        this.sitioweb = sitioweb;
    }

    public Users(Users e){

        this.email = e.email;
        this.name = e.name;
        this.idUser = e.idUser;
        this.id = e.id;
        this.password = e.password;
        this.image = e.image;
        this.banner = e.banner;
        this.bio = e.bio;
        this.location = e.location;
        this.sitioweb = e.sitioweb;

    }

    public void addTweets(List<Tweet> aux){

        for (int i = 0; i < aux.size(); i++) {
            this.tweets.add(aux.get(i));
        }

    }


    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSitioweb() {
        return sitioweb;
    }

    public void setSitioweb(String sitioweb) {
        this.sitioweb = sitioweb;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String id) {
        this.idUser = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void printingData(){

        System.out.println("===== Let's printer all data =====");
        System.out.println("Name: "+getName());
        System.out.println("Id: "+getId());
        System.out.println("IdUser: "+getIdUser());
        System.out.println("Email: "+getEmail());
        System.out.println("Image: "+getImage());
    }

}
