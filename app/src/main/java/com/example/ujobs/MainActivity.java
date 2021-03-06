package com.example.ujobs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.PatternsCompat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    Button register;
    Button auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        register = findViewById(R.id.btnRegister);
        auth = findViewById(R.id.btnAuth);



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrar(view);
            }
        });

        auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth(view);
            }
        });
    }




    // Login
    public void auth(View view){
        Intent auth = new Intent(this, AuthActivity.class);
        startActivity(auth);
    }
    // Options Method
    public void registrar(View view){
        Intent register = new Intent(this,RegisterActivity.class);
        startActivity(register);

    }



}