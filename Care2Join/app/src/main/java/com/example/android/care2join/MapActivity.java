//package com.example.android.care2join;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ListView;
//
//import java.util.ArrayList;
//
///**
// * Created by cyoo0706 on 2/22/17.
// */
//
//public class MapActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.map_activity);
//
//        final ArrayList<Post> browsingList = new ArrayList<>();
//        PostAdapter adapter = new PostAdapter(this, browsingList);
////        adapter.add(new Post("321", "CSE 437", "Olin 1st Floor", "1 hr" ));
////        adapter.add(new Post("543", "CSE 231", "DUC 233", "4 hr, 30 min"));
////        adapter.add(new Post("678", "CSE 131", "Simon 021", "2 hr, 15 min"));
//
//        ListView listView = (ListView) findViewById(R.id.postList);
//        listView.setAdapter(adapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent =  new Intent(MapActivity.this, PostDetailActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
//}
