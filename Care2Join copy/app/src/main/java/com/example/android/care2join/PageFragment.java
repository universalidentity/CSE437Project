package com.example.android.care2join;

import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
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

public class PageFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String ARG_PAGE_NUMBER = "page_number";
    private static final String TAG = "PageFragment";
    private GoogleMap mMap;
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
                initializeMap();
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

    private void initializeMap(){

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


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();

        GoogleMapOptions options = new GoogleMapOptions();
        options.mapType(GoogleMap.MAP_TYPE_NORMAL);
        options.zoomControlsEnabled(true);
        options.zoomGesturesEnabled(true);

        MapFragment.newInstance(options);

        LatLng wustl = new LatLng(38.6, -90.3);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(wustl, 20));
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(wustl));
    }
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
            Log.d(TAG,"no permission");
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            Log.d(TAG,"permission granted");
            mMap.setMyLocationEnabled(true);
        }
    }
    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getActivity(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).

        return false;
    }

}
