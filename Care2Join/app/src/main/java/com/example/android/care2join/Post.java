package com.example.android.care2join;

import java.sql.Time;

/**
 * Created by cyoo0706 on 2/28/17.
 */

public class Post {
    private final int mUserID;
    private String mCourse, mLocation;
    private Time mDuration;
//    private LatLng mCoordinates;


    public Post(int userID, String course, String location, Time duration) {
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

    public Time getmDuration() {
        return mDuration;
    }

    //Setters
    public void setmCourse(String mCourse) {
        this.mCourse = mCourse;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public void setmDuration(Time mDuration) {
        this.mDuration = mDuration;
    }

}
