package com.example.ujobs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button register;
    Button auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = findViewById(R.id.btnRegister);
        auth = findViewById(R.id.btnAuth);
    }

    // Options Method

    // Register
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