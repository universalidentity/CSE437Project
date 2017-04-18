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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;




/**
 * Created by cyoo0706 on 2/24/17.
 */

public class CreatePostActivity extends AppCompatActivity {
    private Button mcreate_post_submit;
    private EditText mclass;
    private EditText mduration;
    private FirebaseAuth mAuth;
    private static final String TAG = "Create_Post";
    private int postid = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_post);




        mcreate_post_submit = (Button) findViewById(R.id.create_post_submit);
        mclass = (EditText) findViewById(R.id.create_post_class);
        mduration = (EditText) findViewById(R.id.create_post_duration);

        if(getIntent().getIntExtra("EditPost",0)==1){
            mclass.setText(getIntent().getStringExtra("mCourse"));
            mduration.setText(getIntent().getStringExtra("mDuration"));
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mDatabase = database.getReference("Posts");
        final DatabaseReference uDatabase = database.getReference("User_Posts");

        mAuth = FirebaseAuth.getInstance();

        mcreate_post_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIntent().getIntExtra("EditPost",0)==1){
                    final FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {


                        String userid = user.getUid();

                        String postkey = getIntent().getStringExtra("mPostID");
                        Post newpost = new Post(postkey, userid, mclass.getText().toString(), mclass.getText().toString(), mduration.getText().toString());
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
                        Post newpost = new Post(postkey, userid, mclass.getText().toString(), mclass.getText().toString(), mduration.getText().toString());
                        mDatabase.child(postkey).setValue(newpost);
                        UserPost newuserpost = new UserPost(postkey);
                        String userkey = uDatabase.child(userid).push().getKey();
                        uDatabase.child(userid).child(userkey).setValue(newuserpost);
                    }
                }

            }
        });

    }
}
/**/