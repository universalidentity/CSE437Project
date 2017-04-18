package com.example.android.care2join;

/**
 * Created by qian on 4/7/17.
 */

public class UserPost {
    private String postid;

    public void UserPost(){
        this.postid = "";
    }
    public UserPost(String postid){
        this.postid = postid;
    }
    public String getPostid(){ return this.postid;}
    public void setPostid(String newid){this.postid = newid;}
}
