package com.example.android.care2join;

import java.sql.Time;

import javax.xml.datatype.Duration;

/**
 * Created by cyoo0706 on 2/28/17.
 */

public class Post {
    private final int mUserID;
    private String mCourse, mLocation, mDuration;
//    private Duration mDuration;
//    private LatLng mCoordinates;


//    public Post(int userID, String course, String location, Duration duration) {
//        this.mUserID = userID;
//        this.mCourse = course;
//        this.mLocation = location;
//        this.mDuration = duration;
//    }

    public Post(int userID, String course, String location, String duration) {
        this.mUserID = userID;
        this.mCourse = course;
        this.mLocation = location;
        this.mDuration = duration;
    }


    //Getters
    public int getmUserID() {
        return mUserID;
    }

    public String getmCourse() {
        return mCourse;
    }

    public String getmLocation() {
        return mLocation;
    }

    public String getmDuration() {
        return mDuration;
    }

//    public Duration getmDuration() {
//        return mDuration;
//    }

    //Setters
    public void setmCourse(String mCourse) {
        this.mCourse = mCourse;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public void setmDuration(String mDuration) {
        this.mDuration = mDuration;
    }
//    public void setmDuration(Duration mDuration) {
//        this.mDuration = mDuration;
//    }

}
