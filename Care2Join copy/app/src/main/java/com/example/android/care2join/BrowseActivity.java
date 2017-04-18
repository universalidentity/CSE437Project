package com.example.android.care2join;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class BrowseActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String TAG = "ListView";
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_activity);




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.newPost);
        FloatingActionButton fab_user = (FloatingActionButton) findViewById(R.id.userProfile);
//        Fragment mapFragment = FragmentManager.findFragmentById(R.id.map);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BrowseActivity.this, CreatePostActivity.class);
                startActivity(intent);
            }
        });

        final ArrayList<Post> browsingList = new ArrayList<>();
        final PostAdapter adapter = new PostAdapter(this, browsingList);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mDatabase = database.getReference("Posts");
        final DatabaseReference uDatabase = database.getReference("User_Posts");
        mAuth = FirebaseAuth.getInstance();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Iterable<DataSnapshot> postlist = dataSnapshot.getChildren();

                for (DataSnapshot eachpost : postlist){
                    Post p = eachpost.getValue(Post.class);
                    Log.d(TAG,"course name: " + p.getmCourse());
                    adapter.add(new Post(p.getmPostID(),p.getmUserID(), p.getmCourse(), p.getmLocation(), p.getmDuration()));

                }


                // [START_EXCLUDE]
//                mAuthorView.setText(post.author);
//                mTitleView.setText(post.title);
//                mBodyView.setText(post.body);
                //Toast(TAG,"Data changed:"+post.)

                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(BrowseActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mDatabase.addValueEventListener(postListener);


//        adapter.add(new Post("321", "CSE 437", "Olin 1st Floor", "1 hr"));
//        adapter.add(new Post("543", "CSE 231", "DUC 233", "4 hr, 30 min"));
//        adapter.add(new Post("678", "CSE 131", "Simon 021", "2 hr, 15 min"));

        ListView listView = (ListView) findViewById(R.id.postList);
        listView.setAdapter(adapter);

        //registerForContextMenu(listView);

        /*final GestureDetector gdt = new GestureDetector(new MyGestureDetector(listView));
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gdt.onTouchEvent(event);
                return true;
            }
        });*/
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(BrowseActivity.this, PostDetailActivity.class);
//                startActivity(intent);
//            }
//        });


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contextmenu,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch(item.getItemId()){
            case R.id.menuEdit:
                Log.d(TAG,"Menu Edit");

                return true;
            case R.id.menuDelete:
                Log.d(TAG,"Menu Delete");
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    //    private static final int SWIPE_MIN_DISTANCE = 120;
//    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
//
//    private class MyGestureDetector extends GestureDetector.SimpleOnGestureListener{
//        private ListView list;
//
//        public MyGestureDetector(ListView list){
//            this.list = list;
//        }
//
//        @Override
//        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                Log.d(TAG, "SWIPE Action detected" );
//                if(showDeleteButton(e1)){
//                    return true;
//                }
//            }
//            return super.onFling(e1, e2, velocityX, velocityY);
//        }
//
//        private boolean showDeleteButton(MotionEvent e1){
//            int pos = list.pointToPosition((int) e1.getX(), (int)e1.getY());
//            return showDeleteButton(pos);
//        }
//        private boolean showDeleteButton(int pos){
//            View child = list.getChildAt(pos);
//            if(child != null){
//                Button delete = (Button) child.findViewById(R.id.deleteButton);
//                if (delete != null)
//                    if (delete.getVisibility() == View.INVISIBLE)
//                        delete.setVisibility(View.VISIBLE);
//                    else
//                        delete.setVisibility(View.INVISIBLE);
//                return true;
//            }
//            return false;
//        }
//    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        GoogleMapOptions options = new GoogleMapOptions();
        options.mapType(GoogleMap.MAP_TYPE_NORMAL);
        options.zoomControlsEnabled(true);
        options.zoomGesturesEnabled(true);
        MapFragment.newInstance(options);

        LatLng wustl = new LatLng(38.6, -90.3);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(wustl, 20));

//        mMap.moveCamera(CameraUpdateFactory.newLatLng(wustl));
    }

}
