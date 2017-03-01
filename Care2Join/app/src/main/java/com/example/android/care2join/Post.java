package com.example.android.care2join;

/**
 * Created by cyoo0706 on 2/28/17.
 */

public class Post {
    private final int mUserID;
    private String mCourse, mLocation;
//    private LatLng mCoordinates;

    public Post(int userID, String course, String location) {
        this.mUserID = userID;
        this.mCourse = course;
        this.mLocation = location;
    }
}
