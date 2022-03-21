package com.example.ujobs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.PatternsCompat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    Button register;
    Button auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        register = findViewById(R.id.btnRegister);
        auth = findViewById(R.id.btnAuth);



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register(view);
            }
        });

        auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth(view);
            }
        });
    }

    // Options Method
    public void register(View view){
        Intent register = new Intent(this, RegisterActivity.class);
        startActivity(register);
    }

    // Login
    public void auth(View view){
        Intent auth = new Intent(this, AuthActivity.class);
        startActivity(auth);
    }

}