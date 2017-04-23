package com.example.android.care2join;

import java.sql.Time;

import javax.xml.datatype.Duration;

/**
 * Created by cyoo0706 on 2/28/17.
 */

public class Post {

    private final String mPostID, mUserID,mEmail;
    private String mCourse,  mDuration;
    private String mLongitude,mLatitude;
//    private Duration mDuration;
//    private LatLng mCoordinates;


//    public Post(int userID, String course, String location, Duration duration) {
//        this.mUserID = userID;
//        this.mCourse = course;
//        this.mLocation = location;
//        this.mDuration = duration;
//    }

    public Post(){
        this.mPostID = "";
        this.mUserID = "";
        this.mCourse = "";
        this.mLongitude = "";
        this.mLatitude = "";
        this.mDuration = "";
        this.mEmail = "";
    }

    public Post(String postID, String userID, String useremail, String course, String latitude, String longitude, String duration) {
        this.mPostID = postID;
        this.mUserID = userID;
        this.mEmail = useremail;
        this.mCourse = course;
        this.mLongitude = longitude;
        this.mLatitude = latitude;
        this.mDuration = duration;
    }


    //Getters
    public String getmPostID() {
        return mPostID;
    }

    public String getmUserID() {return mUserID;}
    public String getmCourse() {
        return mCourse;
    }

    public String getmLongitude() {
        return mLongitude;
    }
    public String getmLatitude() {return mLatitude;}

    public String getmDuration() {
        return mDuration;
    }
    public String getmEmail(){return mEmail;}

//    public Duration getmDuration() {
//        return mDuration;
//    }

    //Setters
    public void setmCourse(String mCourse) {
        this.mCourse = mCourse;
    }

    public void setmLatitude(String mLatitude) {
        this.mLatitude = mLatitude;
    }

    public void setmLongitude(String mLongitude) {
        this.mLongitude = mLongitude;
    }

    public void setmDuration(String mDuration) {
        this.mDuration = mDuration;
    }
//    public void setmDuration(Duration mDuration) {
//        this.mDuration = mDuration;
//    }

}
