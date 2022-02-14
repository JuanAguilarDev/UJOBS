package com.example.ujobs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    Button btnCreate;
    Spinner sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sp = findViewById(R.id.sp);
        btnCreate = findViewById(R.id.btnCrear);

        String [] options = {"Guanajuato", "Michoacan"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
    }

    

}