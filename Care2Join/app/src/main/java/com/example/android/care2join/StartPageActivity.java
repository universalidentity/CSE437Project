package com.example.android.care2join;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by cyoo0706 on 2/22/17.
 */

public class StartPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_page);

        Button logInButton = (Button) findViewById(R.id.login);
        Button createUserButton = (Button) findViewById(R.id.createUser);

        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartPageActivity.this, RegisterUserActivity.class);
                startActivity(intent);
            }
        });

    }
}
