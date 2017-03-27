package com.example.android.care2join;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class BrowseActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_activity);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.newPost);

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
        PostAdapter adapter = new PostAdapter(this, browsingList);
        adapter.add(new Post(321, "CSE 437", "Olin 1st Floor", "1 hr"));
        adapter.add(new Post(543, "CSE 231", "DUC 233", "4 hr, 30 min"));
        adapter.add(new Post(678, "CSE 131", "Simon 021", "2 hr, 15 min"));

        ListView listView = (ListView) findViewById(R.id.postList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BrowseActivity.this, PostDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        GoogleMapOptions options = new GoogleMapOptions();
        options.mapType(GoogleMap.MAP_TYPE_NORMAL);
        options.zoomControlsEnabled(true);
        options.zoomGesturesEnabled(true);
        MapFragment.newInstance(options);

        LatLng wustl = new LatLng(38.6488, -90.3108);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(wustl, 20));

//        mMap.moveCamera(CameraUpdateFactory.newLatLng(wustl));
    }

}
