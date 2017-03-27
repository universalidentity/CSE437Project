package com.example.android.care2join;

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


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mDatabase = database.getReference("Posts");
        final DatabaseReference uDatabase = database.getReference("User_Posts");

        mAuth = FirebaseAuth.getInstance();
        mcreate_post_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CreatePostActivity.this, "Here1",
                        Toast.LENGTH_SHORT).show();
                final FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(CreatePostActivity.this, "Here2",
                            Toast.LENGTH_SHORT).show();
                    Log.d(TAG, user.getUid());
                    // User is signed in
                    String userid = user.getUid();
                    Post newpost = new Post(userid,mclass.getText().toString(), mclass.getText().toString(),mduration.getText().toString());
                    String postkey = mDatabase.push().getKey();
                    mDatabase.child(postkey).setValue(newpost);
                    uDatabase.child(userid).push().setValue(postkey);
                }


            }
        });

    }
}
/**/