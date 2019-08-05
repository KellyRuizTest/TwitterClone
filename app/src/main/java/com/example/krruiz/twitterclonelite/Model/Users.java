package com.example.krruiz.twitterclonelite.Model;

import java.security.PublicKey;
import java.util.List;

public class Users {

    public String email;
    public String name;
    public String id;
    public String password;
    public String image;
    public String banner;
    public String bio;
    public String location;
    public String sitioweb;

    public List <Users> followers;
    public List <Users> followings;

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
        this.id = id;
        this.password = password;
        this.image = imageurl;
        this.banner = bannerurl;
    }

    public Users(String email, String name, String id, String password, String imageurl, String bannerurl, String bio, String location, String sitioweb) {
        this.email = email;
        this.name = name;
        this.id = id;
        this.password = password;
        this.image = imageurl;
        this.banner = bannerurl;
        this.bio = bio;
        this.location = location;
        this.sitioweb = sitioweb;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
