package com.example.android.care2join;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by cyoo0706 on 2/22/17.
 */

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        final ArrayList<Post> browsingList = new ArrayList<>();

        PostAdapter adapter = new PostAdapter(this, browsingList);
        adapter.add(new Post(321, "CSE 437", "Olin 1st Floor", new Time(1, 0, 0)));
        adapter.add(new Post(543, "CSE 231", "DUC 233", new Time(4, 0, 0)));

        ListView listView = (ListView) findViewById(R.id.postList);
        listView.setAdapter(adapter);
//        createUserButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(StartPageActivity.this, RegisterUserActivity.class);
//                startActivity(intent);
//            }
//        });

//        logInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick (View view) {
//                Intent intent = new Intent(StartPageActivity.this, MapActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}
