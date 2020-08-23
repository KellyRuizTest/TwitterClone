package com.example.krruiz.twitterclonelite.Model;

public class Comments {

    private String commenter;
    private String comment;

    public Comments(){
        commenter = "NoComenter";
        comment = "Javaisgood also";
    }

    public Comments(String commenter, String comment) {
        this.commenter = commenter;
        this.comment = comment;
    }

    public void setCommenter(String commenter) {
        this.commenter = commenter;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommenter() {
        return commenter;
    }

    public String getComment() {
        return comment;
    }
}
