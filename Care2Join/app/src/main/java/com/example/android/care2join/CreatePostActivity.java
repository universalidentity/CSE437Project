package com.example.android.care2join;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;


/**
 * Created by cyoo0706 on 2/24/17.
 */

public class CreatePostActivity extends AppCompatActivity implements com.google.android.gms.location.LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private Button mcreate_post_submit;
    private Button mSaveCurrentLocation;
    private EditText mclass;
    private EditText mduration;
    private FirebaseAuth mAuth;
    private static final String TAG = "Create_Post";


    private GoogleApiClient mGoogleApiClient;
    private LocationManager mLocationManager;
    private android.location.LocationListener mLocationListener;
    private LocationRequest mLocationRequest;

    private Location mLastKnownLocation;


    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private int postid = 0;

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_post);

        Bundle extras = getIntent().getExtras();
        Log.d(TAG,"extras null? " + (extras==null));
        if(extras != null) {
            Log.d(TAG,"extras not null");
            mLastKnownLocation = new Location("");
            mLastKnownLocation.setLongitude(extras.getDouble("Longitude"));
            mLastKnownLocation.setLatitude(extras.getDouble("Latitude"));
        }
        mSaveCurrentLocation = (Button) findViewById(R.id.save_location);

        mcreate_post_submit = (Button) findViewById(R.id.create_post_submit);
        mclass = (EditText) findViewById(R.id.create_post_class);
        mduration = (EditText) findViewById(R.id.create_post_duration);

        if(getIntent().getIntExtra("EditPost",0)==1){
            mclass.setText(getIntent().getStringExtra("mCourse"));
            mduration.setText(getIntent().getStringExtra("mDuration"));
            mLastKnownLocation.setLongitude(Double.parseDouble(extras.getString("mLongitude")));
            mLastKnownLocation.setLatitude(Double.parseDouble(extras.getString("mLatitude")));
            Log.d(TAG,"edit last known: " + mLastKnownLocation.getLatitude());
        }
        //start location stuff
        createLocationRequest();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG, "Location has been updated");
                mLastKnownLocation = location;
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        //Checking permissions for LocationManager & LocationListener
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mDatabase = database.getReference("Posts");
        final DatabaseReference uDatabase = database.getReference("User_Posts");

        mAuth = FirebaseAuth.getInstance();

        mSaveCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CreatePostActivity.this, "" + mLastKnownLocation.getLatitude() +
                                ", " + mLastKnownLocation.getLongitude(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        mcreate_post_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIntent().getIntExtra("EditPost",0)==1){
                    final FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {


                        String userid = user.getUid();

                        String postkey = getIntent().getStringExtra("mPostID");
                        String email = user.getEmail().split("@")[0];
                        Post newpost = new Post(postkey, userid, email, mclass.getText().toString(),Double.toString(mLastKnownLocation.getLatitude()),Double.toString(mLastKnownLocation.getLongitude()), mduration.getText().toString());
                        mDatabase.child(postkey).setValue(newpost);

                    }
                }
                else {

                    final FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {

                        Log.d(TAG, user.getUid());
                        // User is signed in
                        String userid = user.getUid();

                        String postkey = mDatabase.push().getKey();
                        String email = user.getEmail().split("@")[0];

                        Post newpost = new Post(postkey, userid,email, mclass.getText().toString(),Double.toString(mLastKnownLocation.getLatitude()),Double.toString(mLastKnownLocation.getLongitude()), mduration.getText().toString());
                        mDatabase.child(postkey).setValue(newpost);
                        UserPost newuserpost = new UserPost(postkey);
                        String userkey = uDatabase.child(userid).push().getKey();
                        uDatabase.child(userid).child(userkey).setValue(newuserpost);
                    }
                }

                Intent intent = new Intent(CreatePostActivity.this, Tab_activity.class);
                startActivity(intent);
            }
        });


    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.v(TAG, "Connection Successful");
        startLocationUpdates();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Play services connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Play services connection suspended");
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "Location update started");
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        Log.d(TAG, "Location updates have stopped");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Location has been updated");
    }
}
/**/