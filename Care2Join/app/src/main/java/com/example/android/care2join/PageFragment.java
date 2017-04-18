package com.example.android.care2join;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import android.location.LocationManager;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by qian on 4/5/17.
 */

public class PageFragment extends Fragment implements LocationListener,OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks{
    private static final String ARG_PAGE_NUMBER = "page_number";
    private static final String TAG = "PageFragment";
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;
    private GoogleApiClient mGoogleApiClient;
    private LocationManager mLocationManager;
    private android.location.LocationListener mLocationListener;
    private LocationRequest mLocationRequest;


    private static final LatLng mDefaultLocation = new LatLng(38.6488, -90.3108);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    private Location mLastKnownLocation;

    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    public PageFragment() {

    }

    public static PageFragment newInstance(int page) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, page);
        fragment.setArguments(args);
        return fragment;
    }

 //   @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        FragmentManager fm = getChildFragmentManager();
//        SupportMapFragment mapFragment = (SupportMapFragment) fm.findFragmentByTag("mapFragment");
//        if (mapFragment == null) {
//            mapFragment = new SupportMapFragment();
//            FragmentTransaction ft = fm.beginTransaction();
//            ft.add(R.id.mapFragmentContainer, mapFragment, "mapFragment");
//            ft.commit();
//            fm.executePendingTransactions();
//        }
//        mapFragment.getMapAsync(this);
//    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //TextView txt = (TextView) rootView.findViewById(R.id.page_number_label);
        int page = getArguments().getInt(ARG_PAGE_NUMBER, -1);
        View rootView;

        switch (page){
            case 1:
                rootView = inflater.inflate(R.layout.fragment_page_layout_map, container, false);
                Log.d(TAG,"initialize Map");
                initializeMap(savedInstanceState);
                return rootView;
            case 2:

                rootView = inflater.inflate(R.layout.fragment_page_layout_list,container,false);
                initializeList(rootView);
                return rootView;
            case 3:
                rootView = inflater.inflate(R.layout.fragment_page_layout_userprofile, container, false);
                initializeProfile(rootView);
                return rootView;

            default:
                rootView = inflater.inflate(R.layout.fragment_page_layout, container, false);
        }

        return rootView;

    }

    private void initializeMap(Bundle savedInstanceState){

        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        createLocationRequest();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity() /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
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
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
        SupportMapFragment mSupportMapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.mapwhere);
        if (mSupportMapFragment == null) {
            Log.d(TAG,"supportfragment null");
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mSupportMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.mapwhere, mSupportMapFragment).commit();
        }
        if (mSupportMapFragment != null)
        {
            Log.d(TAG,"supportfragment not null");
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.mapwhere, mSupportMapFragment).commit();
            mSupportMapFragment.getMapAsync(this);
        }




    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.v(TAG, "Connection Successful");
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Play services connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Play services connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        getDeviceLocation();
    }

    protected void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest,this);
        Log.d(TAG, "Location update started");
    }


    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        Log.d(TAG, "Location updates have stopped");
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void getDeviceLocation() {
        //Checks for permission to access Fine Location in Manifest
        //If permission is not granted, requests from user
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        //Uses FusedLocationAPI to set mLastKnownLocation
        if (mLocationPermissionGranted) {
            Log.d(TAG, "Permission Granted");
            mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }

        // Set the map's camera position to the current location of the device.
        if (mCameraPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (mLastKnownLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
        } else {
            Log.d(TAG, "Current location is null. Using defaults.");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            mMap.addMarker(new MarkerOptions()
                    .title("Wustl")
                    .position(mDefaultLocation));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        mMap.setOnMyLocationButtonClickListener(this);
//        enableMyLocation();
//
//        GoogleMapOptions options = new GoogleMapOptions();
//        options.mapType(GoogleMap.MAP_TYPE_NORMAL);
//        options.zoomControlsEnabled(true);
//        options.zoomGesturesEnabled(true);
//
//        MapFragment.newInstance(options);
//
//        LatLng wustl = new LatLng(38.6, -90.3);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(wustl, 20));
//        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            mMap.setMyLocationEnabled(true);
//        } else {
//            // Show rationale and request permission.
//        }
////        mMap.moveCamera(CameraUpdateFactory.newLatLng(wustl));
//    }
//    private void enableMyLocation() {
//        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            // Permission to access the location is missing.
//            PermissionUtils.requestPermission(getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
//                    Manifest.permission.ACCESS_FINE_LOCATION, true);
//            Log.d(TAG,"no permission");
//        } else if (mMap != null) {
//            // Access to the location has been granted to the app.
//            Log.d(TAG,"permission granted");
//            mMap.setMyLocationEnabled(true);
//        }
//    }



    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Location has been updated");
    }



    private void initializeList(View rootView){
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.newPost);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreatePostActivity.class);
                startActivity(intent);
            }
        });
        final ArrayList<Post> browsingList = new ArrayList<>();
        final PostAdapter adapter = new PostAdapter(getActivity(), browsingList);
        ListView listView = (ListView) rootView.findViewById(R.id.postList);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mDatabase = database.getReference("Posts");
        final DatabaseReference uDatabase = database.getReference("User_Posts");
        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Iterable<DataSnapshot> postlist = dataSnapshot.getChildren();
                adapter.clear();
                for (DataSnapshot eachpost : postlist){
                    Post p = eachpost.getValue(Post.class);
                    Log.d(TAG,"course name: " + p.getmCourse());
                    adapter.add(new Post(p.getmPostID(),p.getmUserID(), p.getmCourse(), p.getmLocation(), p.getmDuration()));

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(getActivity(), "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mDatabase.addValueEventListener(postListener);
        listView.setAdapter(adapter);




    }

    private void initializeProfile(View rootView){
        final ArrayList<Post> browsingList = new ArrayList<>();
        final PostAdapter adapter = new PostAdapter(getActivity(), browsingList);

        ListView listView = (ListView) rootView.findViewById(R.id.postList_userprofile);
        listView.setAdapter(adapter);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        String userid = user.getUid();
        final DatabaseReference mDatabase = database.getReference("Posts");


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                adapter.clear();
                Iterable<DataSnapshot> postlist = dataSnapshot.getChildren();

                for (DataSnapshot eachpost : postlist){
                    Post p = eachpost.getValue(Post.class);
                    Log.d(TAG,"user course name: " + p.getmCourse());
                    adapter.add(new Post(p.getmPostID(),p.getmUserID(), p.getmCourse(), p.getmLocation(), p.getmDuration()));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(getActivity(), "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
            }
        };

        Log.d(TAG,"userid: " + userid);
        Query allUserPosts = mDatabase.orderByChild("mUserID").equalTo(userid);
        allUserPosts.addValueEventListener(postListener);


        listView.setAdapter(adapter);

        registerForContextMenu(listView);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.contextmenu,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        ListView listView= (ListView)info.targetView.getParent();
        Post post = (Post)listView.getAdapter().getItem(info.position);
        String postId = post.getmPostID();
        switch(item.getItemId()){
            case R.id.menuEdit:
                Intent intent = new Intent(getActivity(), CreatePostActivity.class);
                intent.putExtra("EditPost",1);
                intent.putExtra("mPostID",post.getmPostID());
                intent.putExtra("mCourse",post.getmCourse());
                intent.putExtra("mLocation",post.getmLocation());
                intent.putExtra("mDuration",post.getmDuration());
                startActivity(intent);



                return true;
            case R.id.menuDelete:
                Log.d(TAG,"delete info id "+ info.id);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference mDatabase = database.getReference("Posts");
                Query postsToDel = mDatabase.orderByChild("mPostID").equalTo(postId);
                postsToDel.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot singleSanp : dataSnapshot.getChildren()){
                            singleSanp.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    }
                });


                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }


}
